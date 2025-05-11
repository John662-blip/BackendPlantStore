package com.example.Loc.Service;

import com.example.Loc.Modal.BillDetail;
import com.example.Loc.Modal.BillDetail;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface IBillDetailService {
    void delete(BillDetail entity);
    void deleteById(Long id);
    long count();
    <S extends BillDetail> Optional<S> findOne(Example<S> example);
    Optional<BillDetail> findById(Long id);
    List<BillDetail> findAllById(Iterable<Long> ids);
    List<BillDetail> findAll(Sort sort);
    Page<BillDetail> findAll(Pageable pageable);
    List<BillDetail> findAll();
    <S extends BillDetail> S save(S entity);
}
