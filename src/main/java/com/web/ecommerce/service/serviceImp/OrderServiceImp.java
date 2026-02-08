package com.web.ecommerce.service.serviceImp;
import com.web.ecommerce.util.AuthUtil;
import com.web.ecommerce.dto.order.OrderRequest;
import com.web.ecommerce.dto.order.OrderResponse;
import com.web.ecommerce.exceptions.APIException;
import com.web.ecommerce.model.*;
import com.web.ecommerce.model.enums.OrderStatus;
import com.web.ecommerce.repository.*;
import com.web.ecommerce.service.CartService;
import com.web.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImp implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService; // To help with clearing cart or we can do it manually

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        User user = authUtil.loggedInUser();

        Cart cart = cartRepository.findCartByUser(user)
                .orElseThrow(() -> new APIException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new APIException("Cart is empty");
        }

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new APIException("Address not found"));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new APIException("Address does not belong to user");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getUnitPrice());
            orderItem.setDiscount(BigDecimal.ZERO);
            orderItems.add(orderItem);

            BigDecimal itemTotal = orderItem.getOrderedProductPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return modelMapper.map(savedOrder, OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrdersByUser() {
        String email = authUtil.loggedInEmail(); // Or use loggedInUser() if more fields are needed
        List<Order> orders = orderRepository.findByUser_EmailOrderByOrderDateDesc(email);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new APIException("Order not found"));

        User user = authUtil.loggedInUser();

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new APIException("Order does not belong to user");
        }

        return modelMapper.map(order, OrderResponse.class);
    }
}
