package com.example.Loc.Service;

import com.example.Loc.Modal.Account;
import com.example.Loc.Modal.OTP_Token;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IOTP_TokenService {
    void delete(OTP_Token entity);
    void deleteById(Long id);
    long count();
    <S extends OTP_Token> Optional<S> findOne(Example<S> example);
    Optional<OTP_Token> findById(Long id);
    List<OTP_Token> findAllById(Iterable<Long> ids);
    List<OTP_Token> findAll(Sort sort);
    Page<OTP_Token> findAll(Pageable pageable);
    List<OTP_Token> findAll();
    <S extends OTP_Token> S save(S entity);
    String gen_OTP(int length);
    boolean verifyOTP(Long idAccount,String OTP);
    boolean verifyOTP_reset(Long idAccount , String OTP);
}
