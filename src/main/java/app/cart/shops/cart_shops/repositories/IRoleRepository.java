package app.cart.shops.cart_shops.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.cart.shops.cart_shops.models.Role;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(String name);
}
