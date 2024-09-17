package app.cart.shops.cart_shops.constrollers;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.exceptions.CartItemNotFound;
import app.cart.shops.cart_shops.exceptions.ResourceNotFoundException;
import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.models.CartItem;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.cart.ICartItemService;
import app.cart.shops.cart_shops.services.cart.ICartService;
import app.cart.shops.cart_shops.services.user.IUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${value.api.prefix}/cartItems")
public class CartiItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    @GetMapping("/{cartId}/items")
    public ResponseEntity<ApiResponse> getCartItems(@PathVariable Long cartId){
        Cart cart = cartService.getCart(cartId);
        List<CartItem> items=  cart.getCartItems().stream().toList();
        // List<CartItem> cartItems = cartItemService.getCartItem(cartId);
        // return ResponseEntity.ok().body(new ApiResponse("succes", cartItems));
        return null;
    }


    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                    // @RequestParam(required = false) 
                    // Long cartId, 
                    @RequestParam Long productId, 
                    @RequestParam Integer quantity){
            /*
             from here the cart are created
             */

            try {            
                // User user = userService.getUserById(5L);
                User user = userService.getAuthenticatedUser();
                // if the cart not exists, create it with the user
                Cart cart = cartService.initializeNewCart(user);
                cartItemService.addItemToCart(cart.getId(), productId, quantity);
                return ResponseEntity.ok().body(new ApiResponse("Add items success", null));
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            }catch (JwtException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
            }

    }

    @DeleteMapping("/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId,@PathVariable Long itemId){
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok().body(new ApiResponse("Remove succes", null));
            
        } catch (CartItemNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            
        }
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,@PathVariable Long itemId,@RequestParam int quantity){

        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok().body(new ApiResponse("Update success", null));
            
        } catch (CartItemNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
            
        }
    }
}
