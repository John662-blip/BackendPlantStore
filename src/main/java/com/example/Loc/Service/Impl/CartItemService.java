package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Cart;
import com.example.Loc.Modal.CartItem;
import com.example.Loc.Responsitory.CartItemRepository;
import com.example.Loc.Service.ICartItemService;
import com.example.Loc.Service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public <S extends CartItem> S save(S entity) {
        if (entity.getId() == null) {
            return cartItemRepository.save(entity);
        } else {
            Optional<CartItem> opt = findById(entity.getId());
            return cartItemRepository.save(entity);
        }
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemRepository.findById(id);
    }
    @Override
    public <S extends CartItem> Optional<S> findOne(Example<S> example) {
        return cartItemRepository.findOne(example);
    }

    @Override
    public void delete(CartItem entity) {
        cartItemRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
