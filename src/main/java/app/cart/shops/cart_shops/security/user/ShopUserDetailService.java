package app.cart.shops.cart_shops.security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.repositories.IUserRepository;
/*
 userDetailService
 upload user information:
    * This interface defines a method, loadUserByUsername, 
    which is responsible for loading a user's information from a data source.
 create object userDetails:
    * Searches for the user in the data source and creates a userdetails object 
    with the data found.
 interaction with spring security:
    * Spring security uses userDetailsService to get userDetailsService when trying to authenticate.
    */
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopUserDetailService implements UserDetailsService{

    private final IUserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = Optional.ofNullable(userRepository.findByEmail(username))
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // we created UserDetail object implement the UserDetails interface
        return ShopUserDetail.buildUserDetails(user);
    }
    
}
