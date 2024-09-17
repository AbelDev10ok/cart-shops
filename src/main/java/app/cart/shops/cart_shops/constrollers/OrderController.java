package app.cart.shops.cart_shops.constrollers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.dto.OrderDto;
import app.cart.shops.cart_shops.exceptions.ResourceNotFoundException;
import app.cart.shops.cart_shops.models.Order;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.order.IOrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${value.api.prefix}/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId){
        try {
            /*
            when create tha order with the id of the user the cart was already created with the same userId,

            */
            Order order = orderService.placOrder(userId);
            OrderDto orderDto = orderService.mapToDto(order);
            return ResponseEntity.ok().body(new ApiResponse("Item order success",orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error ocurred", e.getMessage()));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId){
        OrderDto order = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(new ApiResponse("success",order));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrder(@PathVariable Long userId){
        try {
            List<OrderDto> orders = orderService.getUserOrders(userId);
            return ResponseEntity.ok().body(new ApiResponse("Item orderIsuccess",orders));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Not found", e.getMessage()));
        }
    }

}
