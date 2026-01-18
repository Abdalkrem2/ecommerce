package com.web.ecommerce.controller;

import com.web.ecommerce.dto.cart.CartItem.CartItemResponse;
import com.web.ecommerce.dto.cart.CartItem.CreateCartItemRequest;
import com.web.ecommerce.dto.cart.CartItem.UpdateCartItemRequest;
import com.web.ecommerce.dto.cart.CartResponse;

import com.web.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private CartService cartService;

//    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/cart/items")
    public ResponseEntity<CartResponse> addItem(@RequestBody @Valid CreateCartItemRequest item) {
        CartResponse response =cartService.addItem(item);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

//    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/cart/items")
    public ResponseEntity<List<CartItemResponse>> getAllItems() {
        List< CartItemResponse> cartItemResponse=cartService.getItems();
        return new ResponseEntity<>(cartItemResponse,HttpStatus.OK);
    }


    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<CartResponse>updateItem(@PathVariable Long cartItemId, @RequestBody @Valid UpdateCartItemRequest updateCartItemRequest) {
        CartResponse cartResponse=cartService.updateItem(cartItemId,updateCartItemRequest);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }
    @DeleteMapping("cart/items/{cartItemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long cartItemId) {
        String status=cartService.deleteItem(cartItemId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

    @DeleteMapping("/cart")
    public ResponseEntity<String> clearCart() {
        String status=cartService.clearCart();
        return new ResponseEntity<>(status,HttpStatus.OK) ;
    }

}
