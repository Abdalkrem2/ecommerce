package com.web.ecommerce.controller;

import com.web.ecommerce.dto.category.CategoryDTO;
import com.web.ecommerce.dto.category.CategoryRequest;
import com.web.ecommerce.dto.category.CategoryResponse;
import com.web.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO=categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);

    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(Pageable pageable) {
        CategoryResponse categoryResponse=categoryService.getAllCategories(pageable);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryDTO categoryDTO=categoryService.updateCategory(categoryId,categoryRequest);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        String status=categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }





}
