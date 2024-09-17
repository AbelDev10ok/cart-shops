package app.cart.shops.cart_shops.repositories;

import app.cart.shops.cart_shops.models.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>{
    Category findByName(String name);
    boolean existsByName(String name);
}
