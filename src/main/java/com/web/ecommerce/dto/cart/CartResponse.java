package com.web.ecommerce.dto.cart;

import com.web.ecommerce.dto.cart.CartItem.CartItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long cartId;
    private Integer totalItems;
    private Integer totalQuantity;
    private BigDecimal totalPrice;

    private List<CartItemResponse> items;

}
