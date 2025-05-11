package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Account;
import com.example.Loc.Modal.BillDetail;
import com.example.Loc.Responsitory.AccountRepository;
import com.example.Loc.Responsitory.BillDetailRepository;
import com.example.Loc.Service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountSercive implements IAccountService {
    @Autowired
    AccountRepository accountRepository;
    //source -> Generate Constructor using Field, xóa super()
//    @Override
    public <S extends Account> S save(S entity) {
        if(entity.getId() == null) {
            return accountRepository.save(entity);
        }else {
            Optional<Account> opt = findById(entity.getId());
            return accountRepository.save(entity);
        }
    }
    public AccountSercive (AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }
    @Override
    public List<Account> findAll(Sort sort) {
        return accountRepository.findAll(sort);
    }
    @Override
    public List<Account> findAllById(Iterable<Long> ids) {
        return accountRepository.findAllById(ids);
    }
    @Override
    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }
    @Override
    public <S extends Account> Optional<S> findOne(Example<S> example) {
        return accountRepository.findOne(example);
    }
    @Override
    public long count() {
        return accountRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
    @Override
    public void delete(Account entity) {
        accountRepository.delete(entity);
    }

    @Override
    public Optional<Account> findByEmail(String email){
        return  accountRepository.findByEmail(email);
    }

    @Override
    public Optional<Account> findByEmailAndPassword(String email, String password) {
        return accountRepository.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<Account> findByUnameAndPassword(String uname, String password) {
        return accountRepository.findByUnameAndPasswordAndAccountStatus(uname, password,1L);
    }

    @Override
    public Optional<Account> findByEmailAndAccount_status(String email, Long status) {
        return accountRepository.findByEmailAndAccountStatus(email,status);
    }

    @Override
    public Optional<Account> findByUnameAndAccount_status(String uname, Long acount_status) {
        return accountRepository.findByUnameAndAccountStatus(uname, acount_status);
    }
}
