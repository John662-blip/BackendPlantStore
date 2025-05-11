package com.example.Loc.Responsitory;

import com.example.Loc.Modal.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContaining(String name);
    //Tìm kiếm và Phân trang
    Page<Product> findByProductNameContaining(String name, Pageable
            pageable);
    Optional<Product> findByProductName(String name);
    Optional<Product> findByCreateDate(Date createAt);
    List<Product> findByCategory_IdAndStatus(Long id,short status);
    Page<Product> findByCategory_IdAndStatus(Long id, short status, Pageable pageable);
    List<Product> findAllByStatusOrderBySoldCountDesc(short status,Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.createDate >= :sevenDaysAgo AND p.status = 1 ORDER BY p.createDate DESC")
    Page<Product> findRecentProducts(Date sevenDaysAgo, Pageable pageable);
    List<Product> findAllByOrderByCreateDateDesc();
}
