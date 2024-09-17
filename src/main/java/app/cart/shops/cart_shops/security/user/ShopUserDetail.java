package app.cart.shops.cart_shops.security.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import app.cart.shops.cart_shops.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
    userDetails (is a data model that represents an authenticated user)
    user representation:
    This interface defines a contract to represent an authenticated user in the application:
        * name of user.
        * Password.
        * Authorities: roles or permissions (ROLE_ADMIN).
        * Estatement.
    Uso en la autenticacion:
        * Spring Security uses userDetails objects to verify user-provided credentials
        during the authentication process.
*/

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShopUserDetail implements UserDetails{

    private Long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // it is a factory that creates a userDetail through a user object
    public static ShopUserDetail buildUserDetails(User user){

        List<GrantedAuthority> authorities = user.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
    return new ShopUserDetail(
            user.getId(),
            user.getEmail(),
            user.getPassword(),
            authorities
            );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    
    
}
