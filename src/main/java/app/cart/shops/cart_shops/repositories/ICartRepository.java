package app.cart.shops.cart_shops.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.cart.shops.cart_shops.models.Cart;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Long>{
    Cart getCartByUserId(Long userId);
}
