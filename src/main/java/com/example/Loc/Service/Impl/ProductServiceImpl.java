package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Category;
import com.example.Loc.Modal.Product;
import com.example.Loc.Responsitory.CategoryRepository;
import com.example.Loc.Responsitory.ProductRepository;
import com.example.Loc.Service.IProductService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductRepository productRepository;

    public <S extends Product> S save(S entity) {
        if(entity.getId() == null) {
            return productRepository.save(entity);
        }else {
            Optional<Product> opt = findById(entity.getId());
            if(opt.isPresent()) {
                if (StringUtils.isEmpty(entity.getImageUrl())) {
                    entity.setImageUrl(
                            opt.get().getImageUrl());
                }else {
                    entity.setImageUrl(entity.getImageUrl());
                }
            }
            return productRepository.save(entity);
        }
    }
    @Override
    public Optional<Product> findByProductName(String name) {
        return productRepository.findByProductName(name);
    }
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<Product> findAll() {
        return productRepository.findAllByOrderByCreateDateDesc();
    }
    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    @Override
    public List<Product> findAll(Sort sort) {
        return productRepository.findAll(sort);
    }
    @Override
    public List<Product> findAllById(Iterable<Long> ids) {
        return productRepository.findAllById(ids);
    }
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    @Override
    public <S extends Product> Optional<S> findOne(Example<S> example) {
        return productRepository.findOne(example);
    }
    @Override
    public long count() {
        return productRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }
    @Override
    public List<Product> findByProductNameContaining(String name) {
        return productRepository.findByProductNameContaining(name);
    }
    @Override
    public Page<Product> findByProductNameContaining(String name, Pageable
            pageable) {
        return productRepository.findByProductNameContaining(name, pageable);
    }
    public List<Product> findByCategory_Id(Long categoryID){
        return  productRepository.findByCategory_IdAndStatus(categoryID,(short)1);
    }

    @Override
    public Page<Product> findByCategory_Id(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findByCategory_IdAndStatus(id,(short) 1,pageable);
    }

    public List<Product> getTop10BestSellingProducts(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "soldCount"));
        return productRepository.findAllByStatusOrderBySoldCountDesc((short) 1,pageable);
    }
    public Page<Product> getRecentProducts(int page, int size) {
        // Lấy ngày hiện tại - 7 ngày
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = calendar.getTime();

        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findRecentProducts(sevenDaysAgo, pageable);
    }

    @Override
    public Page<Product> getPageProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        return productRepository.findAll(pageable);
    }
}
