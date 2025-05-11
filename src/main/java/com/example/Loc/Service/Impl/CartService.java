package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Bill;
import com.example.Loc.Modal.Cart;
import com.example.Loc.Responsitory.BillRespository;
import com.example.Loc.Responsitory.CartRepository;
import com.example.Loc.Service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public <S extends Cart> S save(S entity) {
        if(entity.getId() == null) {
            return cartRepository.save(entity);
        }else {
            Optional<Cart> opt = findById(entity.getId());
            return cartRepository.save(entity);
        }
    }
    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }
    @Override
    public <S extends Cart> Optional<S> findOne(Example<S> example) {
        return cartRepository.findOne(example);
    }

    @Override
    public Optional<Cart> findCartInIdAccount(Long idAcc) {
        return cartRepository.findByIdAccount(idAcc);
    }

    @Override
    public long countItemInCart(Long idAccount) {
        return cartRepository.countCartItemsWithProductStatusOne(idAccount);
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }
}
