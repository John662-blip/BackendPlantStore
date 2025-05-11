package com.example.Loc.Controller;

import com.example.Loc.Modal.Account;
import com.example.Loc.Modal.Response;
import com.example.Loc.Responsitory.AccountRepository;
import com.example.Loc.Service.IAccountService;
import com.example.Loc.Service.IStorageService;
import com.example.Loc.Service.Impl.AccountSercive;
import com.example.Loc.Service.Impl.EmailService;
import com.example.Loc.Untils.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/account")
public class AccountAPIController {
    @Autowired
    Valid valid = new Valid();
    @Autowired
    IAccountService accountService;
    @Autowired
    IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllBill() {
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                accountService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/addAccount")
    public ResponseEntity<?> createAccount(
            @Validated @RequestParam("email") String email,
            @Validated @RequestParam("Uname") String uname,
            @Validated @RequestParam(value = "password",required = false) String password,
            @Validated @RequestParam(value = "name",required = false) String name,
            @Validated @RequestParam(value = "phone",required = false) String phone,
            @Validated @RequestParam("type") Long type,
            @Validated @RequestParam(value = "avatar",required = false) MultipartFile icon,
            @Validated @RequestParam("gender") boolean gender
    ) {
        if (!valid.isValidEmail(email)){
            return
                    new ResponseEntity<Response>(new Response(false, "Email không hợp lệ", null), HttpStatus.BAD_REQUEST);
        }
        Optional<Account> account = accountService.findByEmailAndAccount_status(email,1L);
        if (account.isPresent()){
            return
                    new ResponseEntity<Response>(new Response(false, "Email đã tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        Optional<Account> account1 = accountService.findByUnameAndAccount_status(uname,1L);
        if (account1.isPresent()){
            return
                    new ResponseEntity<Response>(new Response(false, "Username đã tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        Account acc  = new Account();
        if(icon !=null ) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
            acc.setImageUrl(storageService.getSorageFilename(icon,
                    uuString));
            storageService.store(icon, acc.getImageUrl());
        }
        acc.setEmail(email);
        acc.setGender(gender);
        acc.setUname(uname);
        acc.setName(name);
        acc.setPassword(password);
        acc.setPhone(phone);
        acc.setType(type);
        acc.setAccountStatus(0L);
        accountService.save(acc);
        return new ResponseEntity<Response>(new Response(true, "Thêm thành công", acc), HttpStatus.OK);
    }
    @PostMapping(path = "/update_account")
    public ResponseEntity<?> updateAccount(
            @Validated @RequestParam(value = "email",required = false) String email,
            @Validated @RequestParam(value = "password",required = false) String password,
            @Validated @RequestParam(value="name",required = false) String name,
            @Validated @RequestParam(value="phone",required = false) String phone,
            @Validated @RequestParam(value = "type",required = false) Long type,
            @Validated @RequestParam("idAccount") Long idAccount,
            @Validated @RequestParam(value = "avatar",required = false) MultipartFile avatar,
            @Validated @RequestParam(value = "gender",required = false) Boolean gender
    ) {
        if (email!=null && !email.isEmpty()) {
            if (!valid.isValidEmail(email)) {
                return
                        new ResponseEntity<Response>(new Response(false, "Email không hợp lệ", null), HttpStatus.BAD_REQUEST);
            }
        }
        Optional<Account> account = accountService.findById(idAccount);
        if (account.isEmpty()){
            return
                    new ResponseEntity<Response>(new Response(false, "Không tìm thấy thông tin tài khoản", null), HttpStatus.BAD_REQUEST);
        }
        if(avatar != null&&!avatar.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            String uuString = uuid.toString();
//lưu file vào trường Images
            account.get().setImageUrl(storageService.getSorageFilename(avatar, uuString));
            storageService.store(avatar,
                    account.get().getImageUrl());
        }
        if (email != null&&!email.isEmpty()) {
            account.get().setEmail(email);
        }
        if (password != null&&!password.isEmpty()){
            account.get().setPassword(password);
        }
        if (name != null&&!name.isEmpty()){
            account.get().setName(name);
        }
        if (phone != null&&!phone.isEmpty()){
            account.get().setPhone(phone);
        }
        if (type!= null){
            account.get().setType(type);
        }
        if (gender!=null){
            account.get().setGender(gender);
        }
        accountService.save(account.get());
        return new ResponseEntity<Response>(new Response(true, "Cập nhật thành công", account.get()), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(
            @Validated @RequestParam("Uname") String uname,
            @Validated @RequestParam("password") String password
    ) {
       if (uname.isEmpty()||password.isEmpty()){
           return new ResponseEntity<Response>(new Response(false, "Không được để trống email hoặc mật khẩu", null), HttpStatus.BAD_REQUEST);
       }
        Optional<Account> account = accountService.findByUnameAndPassword(uname,password);
        if (!account.isPresent()){
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy thông tin tài khoản", null), HttpStatus.OK);
        }
        if (account.get().getAccountStatus()==1)
            return new ResponseEntity<Response>(new Response(true, "Đăng nhập thành công", account.get()), HttpStatus.OK);
        return new ResponseEntity<Response>(new Response(true, "Tài khoản phải được kích hoạt", account.get()), HttpStatus.OK);
    }
    @PostMapping(path = "/findByID")
    public ResponseEntity<?> findByID(
            @Validated @RequestParam("IdAcount") Long idAccount
    ){
        Optional<Account> account = accountService.findById(idAccount);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Không timg thất tài khoản", null), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(true, "Tìm thấy tài khoản", account.get()), HttpStatus.OK);
    }
    @PostMapping(path = "/findByUnameAndPassword")
    public ResponseEntity<?> findByUnameAndPassword(
            @Validated @RequestParam("uname") String uname,
            @Validated @RequestParam("password") String password
    ){
        Optional<Account> account = accountService.findByUnameAndPassword(uname, password);
        if (account.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Không timg thất tài khoản", null), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(true, "Tìm thấy tài khoản", account.get()), HttpStatus.OK);
    }
}
