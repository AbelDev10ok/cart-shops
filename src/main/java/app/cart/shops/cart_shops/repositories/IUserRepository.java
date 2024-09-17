package app.cart.shops.cart_shops.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.cart.shops.cart_shops.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long>{

    boolean existsByEmail(String email);

    User findByEmail(String username);

}
