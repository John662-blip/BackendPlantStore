package com.example.Loc.Responsitory;

import com.example.Loc.Modal.BillDetail;
import com.example.Loc.Modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail, Long> {
}
