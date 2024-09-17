package app.cart.shops.cart_shops.constrollers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.exceptions.AlredyExistsException;
import app.cart.shops.cart_shops.exceptions.ProductNotFoundException;
import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.dto.ProductDto;
import app.cart.shops.cart_shops.request.AddProductRequest;
import app.cart.shops.cart_shops.request.ProductUpdateRequest;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RequiredArgsConstructor
@RestController
@RequestMapping("${value.api.prefix}/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productsDto = productService.getConvertedProducts(products);
        if(productsDto.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", productsDto));
        }
        return ResponseEntity.ok(new ApiResponse("Products found!", productsDto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest product) {
        try {
            Product entity = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Product created!", entity));
            
        } catch (AlredyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("Error creating product!", e.getMessage()));
        }
    }

    @GetMapping("/products/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand,@RequestParam String name) {
        Long count = productService.countProductsByBrandAndName(brand, name);
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", count));
        }
        return ResponseEntity.ok(new ApiResponse("Count Product for Brand", count));
    }

    


    @GetMapping("/{name}/products")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDto> productsDto = productService.getConvertedProducts(products);

            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", productsDto));
            }
            return ResponseEntity.ok(new ApiResponse("Products found!", products));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductsById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product found!", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDto> productsDto = productService.getConvertedProducts(products);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", productsDto));
            }
            return ResponseEntity.ok(new ApiResponse("Products found!", products));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand,@RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand,name);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", products));
            }
            List<ProductDto> productsDto = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found!", productsDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @GetMapping("/products/{category}/all/products")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", products));
            }
            List<ProductDto> productsDto = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found!", productsDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category,brand);
            if(products.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Products not found!", products));
            }
            List<ProductDto> productsDto = productService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse("Products found!", productsDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest product) {
        try {
            Product productUpdate = productService.updateProduct(product,productId);
            ProductDto productDto = productService.convertToDto(productUpdate);
            return ResponseEntity.ok(new ApiResponse("Product update!", productDto));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProductById(Long productTd){
        try {
            productService.deleteProductById(productTd);
            return ResponseEntity.ok(new ApiResponse("Product deleted!", productTd));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(" not found!", e.getMessage()));
        }
    }
}
