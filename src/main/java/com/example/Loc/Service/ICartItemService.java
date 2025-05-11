package com.example.Loc.Service;

import com.example.Loc.Modal.Cart;
import com.example.Loc.Modal.CartItem;
import com.example.Loc.Modal.Category;
import org.springframework.data.domain.Example;

import java.util.Optional;

public interface ICartItemService {
    <S extends CartItem> Optional<S> findOne(Example<S> example);
    <S extends CartItem> S save(S entity);
    Optional<CartItem> findById(Long id);
    void delete(CartItem entity);
    void deleteById(Long id);
}
