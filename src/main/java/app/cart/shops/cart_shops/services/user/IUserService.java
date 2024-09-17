package app.cart.shops.cart_shops.services.user;

import app.cart.shops.cart_shops.dto.UserDto;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.request.CreateUserRequest;
import app.cart.shops.cart_shops.request.UserUpdateRequest;

public interface IUserService {

// Suggested code may be subject to a license. Learn more: ~LicenseLog:3015496769.
    User getUserById(Long userId);

    User getUserByEmail(String email);

    User createUser(CreateUserRequest user);

    User updateUser(UserUpdateRequest user, Long userId);

    void deleteUser(Long id);

    UserDto mapToDto(User user);

    User getAuthenticatedUser();
}
