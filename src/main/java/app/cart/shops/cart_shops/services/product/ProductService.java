package app.cart.shops.cart_shops.services.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.exceptions.AlredyExistsException;
import app.cart.shops.cart_shops.exceptions.ProductNotFoundException;
import app.cart.shops.cart_shops.models.Category;
import app.cart.shops.cart_shops.models.Product;
import app.cart.shops.cart_shops.repositories.ICategoryRepository;
import app.cart.shops.cart_shops.repositories.IProductRepository;
import app.cart.shops.cart_shops.repositories.IImageRepository;
import app.cart.shops.cart_shops.request.AddProductRequest;
import app.cart.shops.cart_shops.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import app.cart.shops.cart_shops.dto.ProductDto;
import app.cart.shops.cart_shops.models.Image;
import app.cart.shops.cart_shops.dto.ImageDto;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    // al utilizar @requiredargsconstructor de lombok debemos colocarlo como final
    private final IProductRepository productRepository;

    private final ICategoryRepository categoryRepository;

    private final IImageRepository imageRepository;

    private final ModelMapper modelMapper;

    // @Transactional
    @Override
    public Product addProduct(AddProductRequest product) {
        // chek if the category is found in the DB
        // if Tes, set it as the new product category
        // if not, the save the category and set it as the new product category
        if(productExists(product.getName(), product.getBrand())){
            throw new AlredyExistsException("Product already exists!, you many uodate this product");
        }
        Category category = Optional.ofNullable(categoryRepository.findByName(product.getCategory().getName()))
            .orElseGet(() -> { 
                Category newCategory = new Category(product.getCategory().getName());
                return categoryRepository.save(newCategory);
            });

        product.setCategory(category);
        return productRepository.save(createProduct(product, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest product, Category category){
    
        return  new  Product(
            product.getName(),
            product.getBrand(),
            product.getPrice(),
            product.getInventory(),
            product.getDescription(),
            category
        );
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    // @Transactional
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(productRepository::delete,
            // si esta presente al delete le envia la entidad para eliminar
                () -> { throw new ProductNotFoundException("Product not found!");});
        
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    // @Transactional
    @Override
    public Product updateProduct(ProductUpdateRequest product, Long id) {
        return productRepository.findById(id)
            // si existe el producto lo actualiza
            .map(existingProduct -> updateExistingProduct(existingProduct, product))
            // luego de actualizar lo guardamos en la base de datos
            .map(productRepository::save)
            .orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        if (category != null) {
            existingProduct.setCategory(category);
        }

        return existingProduct;
    }   

    
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
            .map(this::convertToDto)
            .toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream().
            map(imag -> 
                modelMapper.map(imag, ImageDto.class))
            .toList();
        productDto.setImages(imageDtos);
        
        return productDto;
    }
}
