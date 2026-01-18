package com.web.ecommerce.dto.product.productImageDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageRequest {
    @NotBlank
    private String url;
    private Boolean isPrimary;
}
