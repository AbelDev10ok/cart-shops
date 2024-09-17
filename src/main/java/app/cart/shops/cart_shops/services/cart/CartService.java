package app.cart.shops.cart_shops.services.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.exceptions.CartNotFound;
import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.repositories.ICartItemRepository;
import app.cart.shops.cart_shops.repositories.ICartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{

    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepositoy;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);


    @Transactional
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
            .orElseThrow(()-> new CartNotFound("Cart Not found!"));
        
        
        BigDecimal totalAmount = cart.getTotalAmount(); 
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepositoy.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
        
    }

    @Override
    public BigDecimal getTotal(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    @Transactional
    @Override
    public Cart initializeNewCart(User user) {
        return Optional.ofNullable(getCartByUserId(user.getId()))
            .orElseGet(()->{
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.getCartByUserId(userId);
    }

        
}
