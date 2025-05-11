package com.example.Loc.Service;

import com.example.Loc.Modal.Account;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    void delete(Account entity);
    void deleteById(Long id);
    long count();
    <S extends Account> Optional<S> findOne(Example<S> example);
    Optional<Account> findById(Long id);
    List<Account> findAllById(Iterable<Long> ids);
    List<Account> findAll(Sort sort);
    Page<Account> findAll(Pageable pageable);
    List<Account> findAll();
    <S extends Account> S save(S entity);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByEmailAndPassword(String email,String password);
    Optional<Account> findByUnameAndPassword(String uname,String password);
    Optional<Account> findByEmailAndAccount_status(String email,Long status);
    Optional<Account> findByUnameAndAccount_status(String uname,Long acount_status);
}
