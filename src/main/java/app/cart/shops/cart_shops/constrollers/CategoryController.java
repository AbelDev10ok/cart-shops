package app.cart.shops.cart_shops.constrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.exceptions.CategoryNotFoundException;
import app.cart.shops.cart_shops.models.Category;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.category.ICategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
@RequestMapping("${value.api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            return ResponseEntity.ok().body(new ApiResponse("success",categoryService.getAllCategories()));
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        try {
            
            Category category = categoryService.getCategoryById(id);
            return ResponseEntity.ok().body(new ApiResponse("success",category));

        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }

    }
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@RequestParam String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok().body(new ApiResponse("success",category));
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody Category category) {
        try {
            categoryService.addCategory(category);
            return ResponseEntity.ok().body(new ApiResponse("success",category));
            
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error",e.getMessage()));
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().body(new ApiResponse("found",null));
            
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
        
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category updateCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok().body(new ApiResponse("update",updateCategory));
            
        } catch (CategoryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error",e.getMessage()));
        }
        
    }
    

    
}
