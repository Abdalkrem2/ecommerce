package com.web.ecommerce.service;

import com.web.ecommerce.dto.category.CategoryDTO;
import com.web.ecommerce.dto.category.CategoryRequest;
import com.web.ecommerce.dto.category.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    CategoryDTO createCategory(CategoryRequest categoryRequest);

    CategoryResponse getAllCategories(Pageable pageable);

    CategoryDTO updateCategory(Long categoryId, @Valid CategoryRequest categoryRequest);

    String deleteCategory(Long categoryId);
}
