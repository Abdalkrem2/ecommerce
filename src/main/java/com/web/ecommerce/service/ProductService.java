package com.web.ecommerce.service;

import com.web.ecommerce.dto.product.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortType);

    ProductDetailsResponse createProduct(ProductRequest productRequest, Long categoryId);

    ProductResponse updateProduct(UpdateProductRequest productRequest, Long productId);

    String deleteProduct(Long productId);

    ProductPageResponse getProductsByCategory(Pageable pageable, Long categoryId);

    @Nullable ProductPageResponse searchProducts(String keyword, Pageable pageable);

    @Nullable ProductPageResponse searchProductsInCategory(Long categoryId, String keyword, Pageable pageable);

    ProductDetailsResponse getProductById(Long productId);
}
