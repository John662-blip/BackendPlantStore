package com.example.Loc.Responsitory;

import com.example.Loc.Modal.Account;
import com.example.Loc.Modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByIdAccount(Long id);

    @Query(value = "SELECT SUM(ci.quantity) FROM cart_item ci " +
            "JOIN cart c ON ci.cart_id = c.id " +
            "JOIN products p ON ci.product_id = p.id " +
            "WHERE c.id_account = :accountId AND p.status = 1", nativeQuery = true)
    long countCartItemsWithProductStatusOne(@Param("accountId") Long accountId);

}
