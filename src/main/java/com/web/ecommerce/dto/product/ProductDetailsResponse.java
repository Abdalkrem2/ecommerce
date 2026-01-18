package com.web.ecommerce.dto.product;

import com.web.ecommerce.dto.product.productImageDTO.ProductImageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailsResponse {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;

    private Long categoryId;
    private String categoryName;

    private List<ProductImageResponse> images;
    private Instant createdAt;
    private Instant updatedAt;
}
