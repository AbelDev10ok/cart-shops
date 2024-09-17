package app.cart.shops.cart_shops.services.user;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.dto.UserDto;
import app.cart.shops.cart_shops.exceptions.AlredyExistsException;
import app.cart.shops.cart_shops.exceptions.ResourceNotFoundException;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.repositories.IUserRepository;
import app.cart.shops.cart_shops.request.CreateUserRequest;
import app.cart.shops.cart_shops.request.UserUpdateRequest;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserByEmail(String email) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @Override
    public User createUser(CreateUserRequest request) {

        return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail()))
        .map(req -> {
            User newUser = new User();
            newUser.setFirstName(req.getFirstName());
            newUser.setLastName(req.getLastName());
            newUser.setEmail(req.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            return userRepository.save(newUser);
        })
        .orElseThrow(() -> new AlredyExistsException("User already exists " + request.getEmail() ));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {

        userRepository.findById(id)
            .ifPresentOrElse(userRepository::delete,()->{
                throw new ResourceNotFoundException("User not found");
            });

        
    }


    @Transactional
    @Override
    public User updateUser(UserUpdateRequest user, Long userId) {
        
        return userRepository.findById(userId)
            .map(u->{
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                return userRepository.save(u);
            })
            .orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public  UserDto mapToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    
}
