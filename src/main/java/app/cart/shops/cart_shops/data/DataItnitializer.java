package app.cart.shops.cart_shops.data;

import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.cart.shops.cart_shops.models.Role;
import app.cart.shops.cart_shops.models.User;
import app.cart.shops.cart_shops.repositories.IRoleRepository;
import app.cart.shops.cart_shops.repositories.IUserRepository;

@Transactional
@Component
@RequiredArgsConstructor
public class DataItnitializer implements ApplicationListener<ApplicationReadyEvent> {
    
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExits();
        createDefaultRoleIfNotExists(defaultRoles);
        createDefaultUserAdminIfNotExits();
    } 


    private void createDefaultUserIfNotExits(){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i = 0; i<5 ; i++){
            String defaultEmail = "user"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User"+1);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("12345"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("User created");
        }
    }

    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new).forEach(roleRepository::save);
    }

    private void createDefaultUserAdminIfNotExits(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        for(int i = 0; i<=2 ; i++){
            String defaultEmail = "admin"+i+"@gmail.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin"+1);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("12345"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user created");
        }
    }
}
