package com.web.ecommerce.service.serviceImp;

import com.web.ecommerce.dto.cart.CartItem.CartItemResponse;
import com.web.ecommerce.dto.cart.CartItem.CreateCartItemRequest;
import com.web.ecommerce.dto.cart.CartItem.UpdateCartItemRequest;
import com.web.ecommerce.dto.cart.CartResponse;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.*;
import com.web.ecommerce.repository.CartItemRepository;
import com.web.ecommerce.repository.CartRepository;
import com.web.ecommerce.repository.ProductRepository;
import com.web.ecommerce.service.CartService;
import com.web.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;



    @Override
    @Transactional
    public CartResponse addItem(CreateCartItemRequest item) {

        User user=authUtil.loggedInUser();
        Cart cart= cartRepository.findCartByUser(user).orElse(null);
        if(cart==null){
            cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);

        }
        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new APIException("Product not found"));
        if(product.getStockQuantity()==null||product.getStockQuantity()<=0){
            throw new APIException("Product out of stock");
        }

        CartItem cartItem=cartItemRepository.findByCartAndProduct(cart,product).orElse(null);
        int quantity=item.getQuantity();
        if(cartItem!=null){
            quantity+=cartItem.getQuantity();
        }
        if(quantity>product.getStockQuantity()){
            throw new APIException("Requested quantity exceeds stock");
        }

        if(cartItem==null){
            cartItem=new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cart.getItems().add(cartItem);

        }
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(product.getPrice());
        cartItemRepository.save(cartItem);

        return toCartResponse(cart);
    }

    @Override
    public List<CartItemResponse> getItems() {
        User user=authUtil.loggedInUser();
        Cart cart=cartRepository.findCartByUser(user).orElseThrow(() -> new APIException("The User Doesnt have cart "));
        List<CartItem> cartItems=cart.getItems();
        return cartItems.stream().map(this::toItemResponse).toList();
    }

    @Override
    public CartResponse updateItem(Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {
        User user = authUtil.loggedInUser();

        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new APIException("Cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException("Cart item not found"));

        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new APIException("This item does not belong to your cart");
        }

        Product product = item.getProduct();
        if (updateCartItemRequest.getQuantity() > product.getStockQuantity()) {
            throw new APIException("Requested quantity exceeds stock");
        }
        item.setQuantity(updateCartItemRequest.getQuantity());
        item.setUnitPrice(product.getPrice());
        cartItemRepository.save(item);
        return toCartResponse(cart);
    }

    @Override
    public String deleteItem(Long cartItemId) {
        User user = authUtil.loggedInUser();
        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new APIException("Cart not found"));

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new APIException("Cart item not found"));
        if (!item.getCart().getCartId().equals(cart.getCartId())) {
            throw new APIException("This item does not belong to your cart");
        }

        cartItemRepository.delete(item);
        return "Item deleted";
    }

    @Override
    public String clearCart() {
        User user = authUtil.loggedInUser();
        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new APIException("Cart not found"));
        if(cart.getItems().isEmpty()){
            throw new APIException("There are no items in the cart");
        }
        cart.getItems().clear();
        cartRepository.save(cart);

        return "Cart cleared";
    }


    //    I used chatgpt for mapping retry doing it alone
    private CartResponse toCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());

        var items = cart.getItems().stream()
                .sorted(Comparator.comparing(CartItem::getCartItemId))
                .map(this::toItemResponse)
                .toList();

        response.setItems(items);
        response.setTotalItems(items.size());

        int totalQty = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        response.setTotalQuantity(totalQty);

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalPrice(total);
        return response;
    }


    private CartItemResponse toItemResponse(CartItem item) {
        CartItemResponse r = new CartItemResponse();
        r.setCartItemId(item.getCartItemId());

        r.setProductId(item.getProduct().getProductId());
        r.setProductName(item.getProduct().getName());

        r.setQuantity(item.getQuantity());
        r.setUnitPrice(item.getUnitPrice());
        r.setSubTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

        // optional: primary image
        String primaryUrl = item.getProduct().getImages().stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .map(ProductImage::getUrl)
                .findFirst()
                .orElse(null);
        r.setPrimaryImageUrl(primaryUrl);

        return r;
    }



}
