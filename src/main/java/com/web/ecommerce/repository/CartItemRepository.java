package com.web.ecommerce.repository;

import com.web.ecommerce.model.Cart;
import com.web.ecommerce.model.CartItem;
import com.web.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional <CartItem> findByCartAndProduct(Cart cart, Product product);
}
