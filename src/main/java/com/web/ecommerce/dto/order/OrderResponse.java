package com.web.ecommerce.dto.order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private String orderStatus;
    private String paymentMethod;
    private Instant orderDate;
}
