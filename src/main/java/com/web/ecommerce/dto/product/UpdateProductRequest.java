package com.web.ecommerce.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    @Size(max = 120)
    private String name;

    @Size(max = 2000)
    private String description;

    @DecimalMin("0.00")
    private BigDecimal price;

    @Min(0)
    private Integer stockQuantity;

    private Boolean active; // only if you have active field in entity
}
