package app.cart.shops.cart_shops.constrollers;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.exceptions.CartNotFound;
import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.cart.ICartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${value.api.prefix}/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId){
        try {
            
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok().body(new ApiResponse("succes", cart));

        } catch (CartNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            
        }
    }

    @DeleteMapping("/{cartId}/clear-cart")  
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId){
        try{
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear cart success",null));   

        }catch (CartNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            
         }
    }

    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId){
        try {
            
            BigDecimal totalPrice =  cartService.getTotal(cartId);
            return ResponseEntity.ok(new ApiResponse("Total price",totalPrice));   
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            
        }
       }


}
