package com.web.ecommerce.dto.order;

import com.web.ecommerce.dto.product.ProductResponse; // Assuming this exists, based on previous analysis
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal orderedProductPrice;
    private BigDecimal discount;
}
