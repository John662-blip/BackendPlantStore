package com.example.Loc.Service;

import com.example.Loc.Modal.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    void delete(Category entity);
    void deleteById(Long id);
    long count();
    <S extends Category> Optional<S> findOne(Example<S> example);
    Optional<Category> findById(Long id);
    List<Category> findAllById(Iterable<Long> ids);
    List<Category> findAll(Sort sort);
    Page<Category> findAll(Pageable pageable);
    List<Category> findAll();
    Optional<Category> findByName(String name);
    <S extends Category> S save(S entity);
    Page<Category> findByNameContaining(String name, Pageable pageable);
    List<Category> findByNameContaining(String name);
}
