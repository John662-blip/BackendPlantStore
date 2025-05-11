package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Bill;
import com.example.Loc.Modal.BillDetail;
import com.example.Loc.Modal.Category;
import com.example.Loc.Responsitory.BillDetailRepository;
import com.example.Loc.Responsitory.CategoryRepository;
import com.example.Loc.Service.IBillDetailService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillDetailService implements IBillDetailService {
    @Autowired
    BillDetailRepository billDetailRepository;
    //source -> Generate Constructor using Field, xóa super()
//    @Override
    public <S extends BillDetail> S save(S entity) {
        if(entity.getId() == null) {
            return billDetailRepository.save(entity);
        }else {
            Optional<BillDetail> opt = findById(entity.getId());
            return billDetailRepository.save(entity);
        }
    }
    public BillDetailService (BillDetailRepository billDetailRepository) {
        this.billDetailRepository = billDetailRepository;
    }
    @Override
    public List<BillDetail> findAll() {
        return billDetailRepository.findAll();
    }
    @Override
    public Page<BillDetail> findAll(Pageable pageable) {
        return billDetailRepository.findAll(pageable);
    }
    @Override
    public List<BillDetail> findAll(Sort sort) {
        return billDetailRepository.findAll(sort);
    }
    @Override
    public List<BillDetail> findAllById(Iterable<Long> ids) {
        return billDetailRepository.findAllById(ids);
    }
    @Override
    public Optional<BillDetail> findById(Long id) {
        return billDetailRepository.findById(id);
    }
    @Override
    public <S extends BillDetail> Optional<S> findOne(Example<S> example) {
        return billDetailRepository.findOne(example);
    }
    @Override
    public long count() {
        return billDetailRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        billDetailRepository.deleteById(id);
    }
    @Override
    public void delete(BillDetail entity) {
        billDetailRepository.delete(entity);
    }
}
