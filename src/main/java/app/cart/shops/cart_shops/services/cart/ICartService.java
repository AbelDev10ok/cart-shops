package app.cart.shops.cart_shops.services.cart;

import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.models.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    Cart getCartByUserId(Long userId);
    void clearCart(Long id);
    BigDecimal getTotal(Long id);
    Cart initializeNewCart(User user);
}
