package app.cart.shops.cart_shops.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    // marca
    private String brand;
    private BigDecimal price;
    // para seguir cuantos selecciono en el carrito
    private int inventory;
    private String description;

    // MUCHOS PRODUCTOS PERTENECEN A UNA CATEGORIA
    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="category_id")
    private Category category;

    /*
    cascadetype.all para que se enfoce en aplicar las demas operaciones en cascada. 
    (persist,merge,detach,refresh,remove).
    a diferencia de orphanremoval solo elimina entidades asocidadas que estan siendo gestionadas por la entidad principal
    esto solo se propaga de la entidad padre al hijo no al reves.
    si se elimina un producto no se eliminara la categoria.
    se utiliza en relaciones manytomany, manytoone, onetomany.
    */
    
    /*
     orphanremoval va mas alla que cascadetyp.all elimina las entidades asociadas que no tengan una referenca
     a otra entidad principal, incluso si no estan siendo gestionadas por la entidad principal
     en otras palabras elimina huerfanos.
     solo se aplica cuando se elimina la entidad padre y no la hija.
     en este caso eliminamos las imagenes cuando se elimine el producto.
     se utiliza en relaciones manytomany, onetomany.
     */
    
     /*
      mappedby indicamos que la relacion es gestionada por la entidad imagen
     */

    //  UN PRODUCTO PUEDE TENER MUCHAS IMAGENES
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Image> images;

    public Product(String name, String brand, BigDecimal price, int inventory, String description, Category category) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.inventory = inventory;
        this.description = description;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", price=" + price + ", inventory="
                + inventory + ", description=" + description + ", category=" + category + ", images=" + images + "]";
    }

    

}
