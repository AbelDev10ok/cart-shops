package app.cart.shops.cart_shops.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.cart.shops.cart_shops.models.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByUserId(Long userId);
    
} 