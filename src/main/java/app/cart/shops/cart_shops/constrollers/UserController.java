package app.cart.shops.cart_shops.constrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.cart.shops.cart_shops.dto.UserDto;
import app.cart.shops.cart_shops.exceptions.AlredyExistsException;
import app.cart.shops.cart_shops.exceptions.ResourceNotFoundException;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.request.CreateUserRequest;
import app.cart.shops.cart_shops.request.UserUpdateRequest;
import app.cart.shops.cart_shops.response.ApiResponse;
import app.cart.shops.cart_shops.services.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("${value.api.prefix}/users")
public class UserController {
    
    private final IUserService userService;

    @GetMapping("/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.mapToDto(user);
            return ResponseEntity.ok().body(new ApiResponse("User found", userDto));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Not found! ",e.getMessage()));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse>  createUser(@RequestBody  CreateUserRequest user) {
        try {
            User userDb = userService.createUser(user);
            UserDto userDto = userService.mapToDto(userDb);

            return ResponseEntity.ok().body(new ApiResponse("User created", userDto));
            
        } catch (AlredyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("User exists! ",e.getMessage()));
        }
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<ApiResponse>  updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest user) {
        try {
            User userDb = userService.updateUser(user,userId);
            UserDto userDto = userService.mapToDto(userDb);
            return ResponseEntity.ok().body(new ApiResponse("User updated", userDto));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Not found! ",e.getMessage()));
        }
    }
    
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse>  deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ApiResponse("User remove",null));
            
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse("Not found! ",e.getMessage()));
        }

    }
}
