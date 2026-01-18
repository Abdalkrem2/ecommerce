package com.web.ecommerce.service.serviceImp;

import com.web.ecommerce.dto.category.CategoryDTO;
import com.web.ecommerce.dto.category.CategoryRequest;
import com.web.ecommerce.dto.category.CategoryResponse;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.Category;
import com.web.ecommerce.repository.CategoryRepository;
import com.web.ecommerce.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryDTO createCategory(CategoryRequest categoryRequest) {
        String name = categoryRequest.getName().trim();
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new APIException("Category already exists");
        }



        Category newCategory = new Category();
        newCategory.setName(categoryRequest.getName());
        newCategory.setDescription(categoryRequest.getDescription());

        newCategory = categoryRepository.save(newCategory);

        return modelMapper.map(newCategory, CategoryDTO.class);
    }


    @Override
    public CategoryResponse getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);


        List<CategoryDTO> content = categoryPage.getContent()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setContent(content);

        return categoryResponse;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new APIException("Category not found"));

        String newName = categoryRequest.getName().trim();


        if (!category.getName().equalsIgnoreCase(newName)
                && categoryRepository.existsByNameIgnoreCase(newName)) {
            throw new APIException("Category name already exists");
        }


        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        category = categoryRepository.save(category);

        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new APIException("Category not found"));

        // if (!category.getProducts().isEmpty()) throw new APIException("Cannot delete category with products");
        categoryRepository.delete(category);
        return "Category deleted Successfully";
    }


}
