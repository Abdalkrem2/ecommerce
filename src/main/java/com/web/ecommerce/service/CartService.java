package com.web.ecommerce.service;

import com.web.ecommerce.dto.cart.CartItem.CartItemResponse;
import com.web.ecommerce.dto.cart.CartItem.CreateCartItemRequest;
import com.web.ecommerce.dto.cart.CartItem.UpdateCartItemRequest;
import com.web.ecommerce.dto.cart.CartResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface CartService {
    CartResponse addItem(@Valid CreateCartItemRequest item);

    List<CartItemResponse> getItems();

    CartResponse updateItem(Long cartItemId, @Valid UpdateCartItemRequest updateCartItemRequest);

    String deleteItem(Long cartItemId);

    String clearCart();
}
