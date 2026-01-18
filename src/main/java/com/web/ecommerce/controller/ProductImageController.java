package com.web.ecommerce.controller;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageRequest;
import com.web.ecommerce.dto.product.productImageDTO.ProductImageResponse;
import com.web.ecommerce.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductImageController {
    @Autowired
    ProductImageService productImageService;

    @GetMapping("/admin/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getProductImages(@PathVariable Long productId) {
       List< ProductImageResponse> productImageResponse = productImageService.getProductImages(productId);
        return new ResponseEntity<>(productImageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> addImages(@PathVariable Long productId, @RequestBody @Valid List<ProductImageRequest> requests) {
        List<ProductImageResponse> productImageResponses=productImageService.addImages(productId, requests);
        return new ResponseEntity<>(productImageResponses, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/products/{productId}/images/{imageId}/primary")
    public ResponseEntity<ProductImageResponse> setPrimary(@PathVariable Long productId, @PathVariable Long imageId) {
        ProductImageResponse productImageResponse=productImageService.setPrimary(productId, imageId);
        return new ResponseEntity<>(productImageResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/products/{productId}/images/{imageId}")
    public ResponseEntity<ProductImageResponse> updateImage(@PathVariable Long productId, @PathVariable Long imageId, @RequestBody ProductImageRequest request) {
        ProductImageResponse productImageResponse=productImageService.updateImage(productId, imageId, request);
        return new ResponseEntity<>(productImageResponse, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/products/{productId}/images/{imageId}")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long productId,
            @PathVariable Long imageId
    ) {
        String status=productImageService.deleteImage(productId, imageId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}


