package com.web.ecommerce.service;

import com.web.ecommerce.dto.product.productImageDTO.ProductImageRequest;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductImageService {
   List< ProductImageResponse> getProductImages(Long productId);

    List<ProductImageResponse> addImages(Long productId, @Valid List<ProductImageRequest> requests);

    ProductImageResponse setPrimary(Long productId, Long imageId);

    ProductImageResponse updateImage(Long productId, Long imageId, ProductImageRequest request);

    String deleteImage(Long productId, Long imageId);
}
