package app.cart.shops.cart_shops.services.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.dto.OrderDto;
import app.cart.shops.cart_shops.enums.OrderStatus;
import app.cart.shops.cart_shops.exceptions.ResourceNotFoundException;
import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.models.Order;
import app.cart.shops.cart_shops.models.OrderItem;
import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.repositories.IOrderRepository;
import app.cart.shops.cart_shops.repositories.IProductRepository;
import app.cart.shops.cart_shops.services.cart.ICartService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    
    @Override
    public OrderDto getOrder(Long orderId) {
        return mapToDto(orderRepository.findById(orderId)
            .orElseThrow(()-> new ResourceNotFoundException("Order not Found!")));
    }



    @Override
    public List<OrderDto> getUserOrders(Long userId){
        return orderRepository.findByUserId(userId).stream()
            .map(this::mapToDto)
            .toList();
    }

    @Transactional
    @Override
    public Order placOrder(Long userId) {

        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);

        // we get the items from the Cartitems in the cart and save them in the order
        List<OrderItem> orderItems = createOrderItems(order, cart);

        BigDecimal totalAmount = calculateTotalAmount(orderItems);
        order.setOrderItems( new HashSet<>(orderItems));
        order.setTotalAmount(totalAmount);
        Order orderSave = orderRepository.save(order);

        // remove the cart
        cartService.clearCart(cart.getId());
        return orderSave;
    }
    
    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        return order;
    }
    
    private List<OrderItem> createOrderItems(Order order, Cart cart){

        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            //update the product inventory
            productRepository.save(product);

            return new OrderItem(
                cartItem.getQuantity(),
                product.getPrice(),
                order,
                product
            );

        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems){
        return orderItems.stream()
            .map(orderItem -> orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public  OrderDto mapToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        return orderDto;
    }
    
}
