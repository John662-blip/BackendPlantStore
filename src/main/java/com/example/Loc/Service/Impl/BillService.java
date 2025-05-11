package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Bill;
import com.example.Loc.Responsitory.BillRespository;
import com.example.Loc.Service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class BillService implements IBillService {
    @Autowired
    BillRespository billRepository;
    //source -> Generate Constructor using Field, xóa super()
//    @Override
    public <S extends Bill> S save(S entity) {
        if(entity.getId() == null) {
            return billRepository.save(entity);
        }else {
            Optional<Bill> opt = findById(entity.getId());
            return billRepository.save(entity);
        }
    }
    public BillService (BillRespository billRepository) {
        this.billRepository = billRepository;
    }
    @Override
    public List<Bill> findAll() {
        return billRepository.findAll();
    }
    @Override
    public Page<Bill> findAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }
    @Override
    public List<Bill> findAll(Sort sort) {
        return billRepository.findAll(sort);
    }
    @Override
    public List<Bill> findAllById(Iterable<Long> ids) {
        return billRepository.findAllById(ids);
    }
    @Override
    public Optional<Bill> findById(Long id) {
        return billRepository.findById(id);
    }
    @Override
    public <S extends Bill> Optional<S> findOne(Example<S> example) {
        return billRepository.findOne(example);
    }
    @Override
    public long count() {
        return billRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        billRepository.deleteById(id);
    }
    @Override
    public void delete(Bill entity) {
        billRepository.delete(entity);
    }

    @Override
    public List<Bill> findByAccountIdOrderByCreatedAtDesc(Long accountId) {
        return billRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    @Override
    public Page<Bill> findPageDate(Date startDate, Date endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return billRepository.findByCreatedAtBetween(startDate,endDate,pageable);
    }

    @Override
    public List<Object[]> getMonthlyRevenueByYear(int year) {
        return billRepository.getMonthlyRevenueByYear(year);
    }

    @Override
    public List<Object[]> findTopSellingProductsByMonth(int year, int month, Pageable pageable) {
        return billRepository.findTopSellingProductsByMonth(year,month,pageable);
    }
}
