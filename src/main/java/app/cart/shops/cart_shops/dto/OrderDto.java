package app.cart.shops.cart_shops.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import app.cart.shops.cart_shops.models.Cart;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDto> items;
}
