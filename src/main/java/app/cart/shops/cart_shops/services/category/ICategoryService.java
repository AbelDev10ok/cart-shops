package app.cart.shops.cart_shops.services.category;

import java.util.List;

import org.springframework.stereotype.Service;

import app.cart.shops.cart_shops.models.Category;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
}
