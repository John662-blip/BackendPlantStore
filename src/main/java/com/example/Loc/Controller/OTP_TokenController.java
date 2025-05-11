package com.example.Loc.Controller;

import com.example.Loc.Modal.Account;
import com.example.Loc.Modal.Cart;
import com.example.Loc.Modal.OTP_Token;
import com.example.Loc.Modal.Response;
import com.example.Loc.Service.IAccountService;
import com.example.Loc.Service.ICartService;
import com.example.Loc.Service.IEmailService;
import com.example.Loc.Service.IOTP_TokenService;
import com.example.Loc.Untils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/otp_token")
public class OTP_TokenController {
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    IEmailService iEmailService;
    @Autowired
    IAccountService iAccountService;
    @Autowired
    IOTP_TokenService iotpTokenService;
    @Autowired
    ICartService iCartService;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                iotpTokenService.findAll()), HttpStatus.OK);
    }
    @PostMapping(path = "/sendOTP_CreateAccount")
    public ResponseEntity<?> sendOTP_Create(
            @Validated @RequestParam("idAccount") Long idAcc
    ){
        String OTP =iotpTokenService.gen_OTP(5);
        Optional<Account> account = iAccountService.findById(idAcc);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "ko tim thay id", null), HttpStatus.BAD_REQUEST);
        }
        Optional<Account> account1 = iAccountService.findByUnameAndAccount_status(account.get().getUname(),1L);
        if (account1.isPresent()){
            return new ResponseEntity<Response>(new Response(false, "email da hoat dong", null), HttpStatus.BAD_REQUEST);
        }
        Optional<Account> account2 = iAccountService.findByEmailAndAccount_status(account.get().getEmail(),1L);
        if (account2.isPresent()){
            return new ResponseEntity<Response>(new Response(false, "UserName da hoat dong", null), HttpStatus.BAD_REQUEST);
        }

        OTP_Token token = new OTP_Token();
        Date now = new Date();
        long expiresInMillis = now.getTime() + 10 * 60 * 1000;
        OTP_Token otpToken = new OTP_Token();
        otpToken.setIdAccount(account.get().getId());
        otpToken.setOtp_code(OTP);
        otpToken.setCreated_at(now);
        otpToken.setExpires_at(new Date(expiresInMillis));
        otpToken.setType(1L);
        otpToken.set_verified(false);
        iotpTokenService.save(otpToken);
        iEmailService.SentOTP_Confirm(account.get().getEmail(),OTP);
        return new ResponseEntity<Response>(new Response(true
                , "Gui Thanh Cong", account.get()), HttpStatus.OK);
    }

    @PostMapping(path = "/sendOTP_Reset")
    public ResponseEntity<?> sendOTP_Reset(
            @Validated @RequestParam("email") String email
    ){
        String OTP =iotpTokenService.gen_OTP(5);
        Optional<Account> account = iAccountService.findByEmailAndAccount_status(email,1L);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Khong tim thay tai khoan", null), HttpStatus.BAD_REQUEST);
        }
        if (account.get().getAccountStatus()!=1L){
            return new ResponseEntity<Response>(new Response(false, "Tai khoan nay khong hoa dong", null), HttpStatus.BAD_REQUEST);
        }
        OTP_Token token = new OTP_Token();
        Date now = new Date();
        long expiresInMillis = now.getTime() + 10 * 60 * 1000;
        OTP_Token otpToken = new OTP_Token();
        otpToken.setIdAccount(account.get().getId());
        otpToken.setOtp_code(OTP);
        otpToken.setCreated_at(now);
        otpToken.setExpires_at(new Date(expiresInMillis));
        otpToken.setType(2L);
        otpToken.set_verified(false);
        iotpTokenService.save(otpToken);
        iEmailService.SendOTP_ResetPassword(email,OTP);
        return new ResponseEntity<Response>(new Response(true
                , "Gui Thanh Cong", account.get()), HttpStatus.OK);
    }
    @PostMapping(path = "/findForget")
    public ResponseEntity<?> findForget(
            @Validated @RequestParam("email") String email
    ) {
        Optional<Account> account = iAccountService.findByEmailAndAccount_status(email, 1L);
        if (account.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Khong tim thay tai khoan", null), HttpStatus.BAD_REQUEST);
        }
        if (account.get().getAccountStatus() != 1L) {
            return new ResponseEntity<Response>(new Response(false, "Tai khoan nay khong hoa dong", null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Response>(new Response(true
                , "Gui Thanh Cong", account.get()), HttpStatus.OK);
    }
    @PostMapping(path = "/comfirm_OTP")
    public ResponseEntity<?> verifyOTP_comfirm_create(
            @Validated @RequestParam("idAccount") Long idAccount,
            @Validated @RequestParam("otp") String otp
    ){
        Optional<Account> account = iAccountService.findById(idAccount);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false
                    , "Không tìm thấy Account", account.get()), HttpStatus.OK);
        }
        Optional<Account> account1 = iAccountService.findByEmailAndAccount_status(account.get().getEmail(),1L);
        if (account1.isPresent()){
            return new ResponseEntity<Response>(new Response(false
                    , "Da co tai khoan hoat dong", account.get()), HttpStatus.OK);
        }
        if (iotpTokenService.verifyOTP(idAccount,otp)){
            account.get().setAccountStatus(1L);
            iAccountService.save(account.get());
            Cart cart = new Cart();
            cart.setIdAccount(account.get().getId());
            iCartService.save(cart);
            return new ResponseEntity<Response>(new Response(true
                    , "Xác thực thành công", account.get()), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(false
                , "Xác sai mã OTP", account.get()), HttpStatus.OK);
    }
    @PostMapping(path = "/reset_OTP")
    public ResponseEntity<?> verifyOTP_resetPassword(
            @Validated @RequestParam("idAccount") Long idAccount,
            @Validated @RequestParam("otp") String otp
    ){
        Optional<Account> account = iAccountService.findById(idAccount);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false
                    , "Không tìm thấy Account", account.get()), HttpStatus.OK);
        }
        if (iotpTokenService.verifyOTP_reset(idAccount,otp)){
            return new ResponseEntity<Response>(new Response(true
                    , "Xác thực thành công", account.get()), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(false
                , "Xác sai mã OTP", account.get()), HttpStatus.OK);
    }
}
