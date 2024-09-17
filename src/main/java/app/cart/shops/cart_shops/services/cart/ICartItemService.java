package app.cart.shops.cart_shops.services.cart;

import java.util.List;

import app.cart.shops.cart_shops.models.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItem(Long cartId, Long productId);
    // CartItem getCartItem(Long cartId, Long productId);
    // List<CartItem> getCartItems(Long cartId);
    // void clearCart(Long cartId);
    // double getCartTotal(Long cartId);
}
