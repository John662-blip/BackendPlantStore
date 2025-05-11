package com.example.Loc.Service;

import com.example.Loc.Modal.Bill;
import com.example.Loc.Modal.BillDetail;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IBillService {
    void delete(Bill entity);
    void deleteById(Long id);
    long count();
    <S extends Bill> Optional<S> findOne(Example<S> example);
    Optional<Bill> findById(Long id);
    List<Bill> findAllById(Iterable<Long> ids);
    List<Bill> findAll(Sort sort);
    Page<Bill> findAll(Pageable pageable);
    List<Bill> findAll();
    <S extends Bill> S save(S entity);
    List<Bill> findByAccountIdOrderByCreatedAtDesc(Long accountId);
    Page<Bill> findPageDate(Date startDate,Date endDate,int page,int size);
    List<Object[]> getMonthlyRevenueByYear(int year);
    List<Object[]> findTopSellingProductsByMonth(
            int year,
            int month,
            Pageable pageable);
}
