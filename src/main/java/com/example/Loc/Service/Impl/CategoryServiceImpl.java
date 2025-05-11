package com.example.Loc.Service.Impl;

import com.example.Loc.Modal.Category;
import com.example.Loc.Responsitory.CategoryRepository;
import com.example.Loc.Service.ICategoryService;
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
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    //source -> Generate Constructor using Field, xóa super()
//    @Override
    public <S extends Category> S save(S entity) {
        if(entity.getId() == null) {
            return categoryRepository.save(entity);
        }else {
            Optional<Category> opt = findById(entity.getId());
            if(opt.isPresent()) {
                if (StringUtils.isEmpty(entity.getImageUrl())) {
                    entity.setImageUrl(
                            opt.get().getImageUrl());
                }else {
                    entity.setImageUrl(entity.getImageUrl());
                }
            }
            return categoryRepository.save(entity);
        }
    }
    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    @Override
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    @Override
    public List<Category> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }
    @Override
    public List<Category> findAllById(Iterable<Long> ids) {
        return categoryRepository.findAllById(ids);
    }
    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    @Override
    public <S extends Category> Optional<S> findOne(Example<S> example) {
        return categoryRepository.findOne(example);
    }
    @Override
    public long count() {
        return categoryRepository.count();
    }
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    @Override
    public void delete(Category entity) {
        categoryRepository.delete(entity);
    }
    @Override
    public List<Category> findByNameContaining(String name) {
        return categoryRepository.findByNameContaining(name);
    }
    @Override
    public Page<Category> findByNameContaining(String name, Pageable
            pageable) {
        return categoryRepository.findByNameContaining(name, pageable);
    }
}
