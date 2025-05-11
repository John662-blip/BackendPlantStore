package com.example.Loc.Responsitory;

import com.example.Loc.Modal.Bill;
import com.example.Loc.Modal.BillDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BillRespository extends JpaRepository<Bill, Long> {
    List<Bill> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    @Query("SELECT b FROM Bill b WHERE b.createdAt BETWEEN :startDate AND :endDate")
    Page<Bill> findByCreatedAtBetween(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );
    @Query("SELECT MONTH(b.createdAt) AS month, SUM(b.total) AS totalRevenue " +
            "FROM Bill b " +
            "WHERE YEAR(b.createdAt) = :year AND b.status = 2 " +
            "GROUP BY MONTH(b.createdAt) " +
            "ORDER BY month")
    List<Object[]> getMonthlyRevenueByYear(@Param("year") int year);

    @Query("SELECT bd.product.id, bd.product.productName, SUM(bd.quantity) AS totalSold " +
            "FROM BillDetail bd " +
            "JOIN bd.bill b " +
            "WHERE YEAR(b.createdAt) = :year AND MONTH(b.createdAt) = :month AND b.status = 2 " +
            "GROUP BY bd.product.id, bd.product.productName " +
            "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingProductsByMonth(
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable);
}
