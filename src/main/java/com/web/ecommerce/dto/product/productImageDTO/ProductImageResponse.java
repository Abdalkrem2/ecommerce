package com.web.ecommerce.dto.product.productImageDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponse {
    private Long imageId;
    private String url;
    private Boolean isPrimary;
}
