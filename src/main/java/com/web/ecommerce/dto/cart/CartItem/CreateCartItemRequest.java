package com.web.ecommerce.dto.cart.CartItem;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCartItemRequest {
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
}
