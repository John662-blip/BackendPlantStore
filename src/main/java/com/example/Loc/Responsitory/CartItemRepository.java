package com.example.Loc.Responsitory;

import com.example.Loc.Modal.CartItem;
import com.example.Loc.Modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
