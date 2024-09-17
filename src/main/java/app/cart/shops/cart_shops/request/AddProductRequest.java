package app.cart.shops.cart_shops.request;

import java.math.BigDecimal;

import app.cart.shops.cart_shops.models.Category;
import lombok.Data;

/*
utilizamos esta clase ya que no es recomendable trabajar con la clase entidad producto.
de esta forma evitamos errores
*/ 
@Data
/*
en este caso podemos trabajar con @data de lombok que nos agregar get, set, hash, equal, tostring y constructor
*/
public class AddProductRequest {
    private Long id;
    private String name;
    // marca
    private String brand;
    private BigDecimal price;
    // para seguir cuantos selecciono en el carrito
    private int inventory;
    private String description;
    private Category category;
}
