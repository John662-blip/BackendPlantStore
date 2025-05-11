package com.example.Loc.Controller;

import com.example.Loc.Modal.Category;
import com.example.Loc.Modal.Response;
import com.example.Loc.Service.ICategoryService;
import com.example.Loc.Service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/category")
public class CategoryAPIController {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    IStorageService storageService;
    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                categoryService.findAll()), HttpStatus.OK);
    }

    @PostMapping(path = "/getCategory")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<Response>(new Response(true, "Thành công",
                    category.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<Response>(new Response(false, "Thất bại", null), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(path = "/addCategory")
    public ResponseEntity<?> addCategory(@Validated @RequestParam("categoryName")
                                         String categoryName,
                                         @Validated @RequestParam(value = "icon",required = false) MultipartFile icon) {
        Optional<Category> optCategory =
                categoryService.findByName(categoryName);
        if (optCategory.isPresent()) {
            return
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category đã tồn tại trong hệ thống");
        } else {
            Category category = new Category();
            if(icon!= null &&!icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
                category.setImageUrl(storageService.getSorageFilename(icon,
                        uuString));
                storageService.store(icon, category.getImageUrl());
            }
            category.setName(categoryName);
            categoryService.save(category);
            return new ResponseEntity<Response>(new Response(true, "Thêm Thành công", category), HttpStatus.OK);
        }
    }
    @PutMapping(path = "/updateCategory")
    public ResponseEntity<?> updateCategory(@Validated @RequestParam("categoryId")
                                            Long categoryId,
                                            @Validated @RequestParam("categoryName") String categoryName,
                                            @Validated @RequestParam(value = "icon",required = false) MultipartFile icon) {
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }else if(optCategory.isPresent()) {
//kiểm tra tồn tại file, lưu file
            if(icon!= null && !icon.isEmpty()) {
                UUID uuid = UUID.randomUUID();
                String uuString = uuid.toString();
//lưu file vào trường Images
                optCategory.get().setImageUrl(storageService.getSorageFilename(icon, uuString));
                storageService.store(icon,
                        optCategory.get().getImageUrl());
            }
            optCategory.get().setName(categoryName);
            categoryService.save(optCategory.get());
            return new ResponseEntity<Response>(new
                    Response(true, "Cập nhật Thành công", optCategory.get()), HttpStatus.OK);
        }
        return null;
    }
    @DeleteMapping(path = "/deleteCategory")
    public ResponseEntity<?> deleteCategory(@Validated @RequestParam("categoryId")
                                            Long categoryId){
        Optional<Category> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Category", null), HttpStatus.BAD_REQUEST);
        }else if(optCategory.isPresent()) {
            categoryService.delete(optCategory.get());
//return ResponseEntity.ok().body(category);
            return new ResponseEntity<Response>(new Response(true, "Xóa Thành công", null), HttpStatus.OK);
        }
        return null;
    }
}
