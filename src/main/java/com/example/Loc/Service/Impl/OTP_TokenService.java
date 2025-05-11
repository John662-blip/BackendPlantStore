package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.OTP_Token;
import com.example.Loc.Responsitory.OTP_TokenRepository;
import com.example.Loc.Service.IOTP_TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OTP_TokenService implements IOTP_TokenService {
    @Autowired
    OTP_TokenRepository otp_tokenRepository;
    //source -> Generate Constructor using Field, xóa super()
//    @Override
    public <S extends OTP_Token> S save(S entity) {
        if(entity.getId() == null) {
            return otp_tokenRepository.save(entity);
        }else {
            Optional<OTP_Token> opt = findById(entity.getId());
            return otp_tokenRepository.save(entity);
        }
    }
    public OTP_TokenService (OTP_TokenRepository otp_tokenRepository) {
        this.otp_tokenRepository = otp_tokenRepository;
    }
    @Override
    public List<OTP_Token> findAll() {
        return otp_tokenRepository.findAll();
    }
    @Override
    public Page<OTP_Token> findAll(Pageable pageable) {
        return otp_tokenRepository.findAll(pageable);
    }
    @Override
    public List<OTP_Token> findAll(Sort sort) {
        return otp_tokenRepository.findAll(sort);
    }
    @Override
    public List<OTP_Token> findAllById(Iterable<Long> ids) {
        return otp_tokenRepository.findAllById(ids);
    }
    @Override
    public Optional<OTP_Token> findById(Long id) {
        return otp_tokenRepository.findById(id);
    }
    @Override
    public <S extends OTP_Token> Optional<S> findOne(Example<S> example) {
        return otp_tokenRepository.findOne(example);
    }
    @Override
    public long count() {
        return otp_tokenRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        otp_tokenRepository.deleteById(id);
    }
    @Override
    public void delete(OTP_Token entity) {
        otp_tokenRepository.delete(entity);
    }

    @Override
    public String gen_OTP(int length) {
        String OTP = "";
        Random rand = new Random();
        for(int i=0; i<length; i++){
            OTP += rand.nextInt(10);
        }
        return OTP;
    }

    @Override
    public boolean verifyOTP(Long idAccount, String OTP) {
        return otp_tokenRepository.verifyOTP(idAccount,OTP);
    }

    @Override
    public boolean verifyOTP_reset(Long idAccount, String OTP) {
        return otp_tokenRepository.verifyOTP_reset(idAccount,OTP);
    }
}
