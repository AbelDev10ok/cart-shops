package app.cart.shops.cart_shops.services.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.exceptions.ProductNotFoundException;
import app.cart.shops.cart_shops.models.Cart;
import app.cart.shops.cart_shops.models.CartItem;
import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.repositories.ICartItemRepository;
import app.cart.shops.cart_shops.repositories.ICartRepository;
import app.cart.shops.cart_shops.services.product.IProductService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CartItemService implements ICartItemService{

    private final ICartItemRepository cartItemRepository;
    private final ICartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    @Transactional
    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1. Get the cart
        //2. Get the product
        //3. Chekc of the product already inthe cart
        //5. If NO, the initiate a new CartItem entry
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);
        
        //4. If YES, then increase the quantity with the request quantity
        CartItem cartItem = cart.getCartItems()
            .stream()
            .filter(item -> 
                item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());
        if(cartItem.getId() == null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        // update total price of cartItems
        cartItem.setTotalPrice();
        // add items to cart
        cart.addItem(cartItem);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemRemove = getCartItem(cartId, productId);
        cart.removeItem(itemRemove);
        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
            cart.getCartItems()
                .stream()
                .filter(item -> 
                    item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item ->{
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

            // update total amount
            BigDecimal totalAmount = cart.getCartItems()
                                    .stream()
                                    .map(CartItem::getTotalPrice)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    
            cart.setTotalAmount(totalAmount);
            cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems()
            .stream()
            .filter(item -> 
                item.getProduct().getId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new ProductNotFoundException("Product  not found"));
    }
    
}
