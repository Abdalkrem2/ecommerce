package com.web.ecommerce.service;
import com.web.ecommerce.dto.order.OrderRequest;
import com.web.ecommerce.dto.order.OrderResponse;
import java.util.List;
public interface OrderService {
    OrderResponse placeOrder(OrderRequest orderRequest);

    List<OrderResponse> getOrdersByUser();

    OrderResponse getOrder(Long orderId);
}
