package com.example.Loc.Service;

import com.example.Loc.Modal.BillDetail;
import com.example.Loc.Modal.Cart;
import com.example.Loc.Modal.Category;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface ICartService {
    <S extends Cart> Optional<S> findOne(Example<S> example);
    <S extends Cart> S save(S entity);
    Optional<Cart> findCartInIdAccount(Long idAcc);
    Optional<Cart> findById(Long id);
    long countItemInCart(Long idAccount);
    List<Cart> findAll();
}
