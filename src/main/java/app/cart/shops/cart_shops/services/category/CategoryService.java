package app.cart.shops.cart_shops.services.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.cart.shops.cart_shops.exceptions.CategoryNotFoundException;
import app.cart.shops.cart_shops.models.Category;
import app.cart.shops.cart_shops.repositories.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryService implements ICategoryService{

    private final ICategoryRepository categoryRepository;

    
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> 
        new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        // recorro las categorias para ver si existe alguna con el mismo nombre
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
        // si no existe ninguna la creo
        .map(categoryRepository::save)
        // si existe exception
        .orElseThrow(() -> new CategoryNotFoundException("Category already exists with name: " + category.getName()));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
        .ifPresentOrElse((categoryRepository::delete), () -> {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        });        
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
        .map(existingCategory -> {
            existingCategory.setName(category.getName());
            return categoryRepository.save(existingCategory);
        })
        .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

}
