package app.cart.shops.cart_shops.services.product;

import java.util.List;

import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.request.AddProductRequest;
import app.cart.shops.cart_shops.request.ProductUpdateRequest;
import app.cart.shops.cart_shops.dto.ProductDto;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);

    // ejemplo iphone telefono
    List<Product> getProductsByBrandAndName(String brand, String name);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);

    // ejemplo contar celulares samsung
    Long countProductsByBrandAndName(String brand, String name);
}
