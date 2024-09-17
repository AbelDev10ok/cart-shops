package app.cart.shops.cart_shops.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import app.cart.shops.cart_shops.models.Image;

@Repository
public interface IImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long productId);
}
