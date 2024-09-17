package app.cart.shops.cart_shops.services.order;

import java.util.List;

import app.cart.shops.cart_shops.dto.OrderDto;
import app.cart.shops.cart_shops.models.Order;

public interface IOrderService {
    Order placOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto mapToDto(Order order);
}
