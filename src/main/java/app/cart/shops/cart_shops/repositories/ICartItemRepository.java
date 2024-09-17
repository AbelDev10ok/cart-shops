package app.cart.shops.cart_shops.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.cart.shops.cart_shops.models.CartItem;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);
}
