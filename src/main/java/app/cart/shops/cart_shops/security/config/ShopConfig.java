package app.cart.shops.cart_shops.security.config;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import app.cart.shops.cart_shops.security.jwt.AuthTokenFilter;
import app.cart.shops.cart_shops.security.jwt.JwtAuthEntryPoint;
import app.cart.shops.cart_shops.security.user.ShopUserDetailService;
import lombok.RequiredArgsConstructor;
/*
 ¡Excelente pregunta! Entender el flujo de ejecución es clave para comprender cómo funciona la autenticación en tu aplicación.

Veamos el paso a paso de la ejecución de estas clases durante la autenticación de un usuario:

 1_ Login: The user tries to log in to your application, providing their email and password.

 2_ AuthenticationManager: Spring Security uses an AuthenticationManager to manage the authentication process.

 3_ ShopUserDetailService:
    * The AuthenticationManager delegates the user lookup to ShopUserDetailService.
    * ShopUserDetailService.loadUserByUsername(email) is executed, searching for the user in the database by their email
    * If the user is found, a ShopUserDetail object is created with the user's information and roles.
 
4_ Password comparison:
    * The AuthenticationManager compares the password provided by the user with the password
      stored in the ShopUserDetail object (which was obtained from the database).
    * If the passwords match, the authentication is successful.

5_ JwtUtils:
    * If authentication is successful, an Authentication object is created that represents the authenticated user.
    * This Authentication object is passed to JwtUtils.generateTokenForUser(authentication).
    * JwtUtils generates a JWT token containing information about the user (email, id, roles).

6_ Token JWT:
    * The JWT token is returned to the user (usually in the Authorization header of the HTTP response).

7_ Subsequent requests:
    * In subsequent requests that the user makes(realice) to your application, the JWT token is sent in the Authorization header.
    * A filter (JwtAuthenticationFilter) intercepts the request and validate the JWT token using JwtUtils.validateToken(token).
    * If the token is valid, the user's authentication to Spring Security is established(establece) for the current request

In short(resumen): ShopUserDetailService is run at login to get the user information. 
JwtUtils is run after successful authentication to generate the JWT token, and is also(tambien) used in each(cada) request to validate the token.
 */

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class ShopConfig {
    private final ShopUserDetailService shopUserDetailService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private static final List<String> SECURED_URLS = 
        List.of("/api/v1/cart/**","/api/v1/cartItem/**");


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authCong) throws Exception{
        return authCong.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(shopUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        /*
        csrf disable the protecion against csrf attacks.
        In applications that use JWT (JSON Web Token) for authentication, CSRF is often(amenudo) disabled because(porque) 
        JWTs are inherently resistant to this type of attack.
        */
        http.csrf(AbstractHttpConfigurer::disable)
            // configuration of exceptions handle
            .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint))
            /* Configuration of session management
              Define the session management policy as "stateless" (STATELESS) sin estado
              This is essential for applications that use JWTs, since(ya que) JWTs contain all the information necessary for authentication 
              and no session information needs to be stored on the server.
           */ 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(SECURED_URLS.toArray(String[]::new)).authenticated()
                .anyRequest().permitAll());
            // Set the authentication provider to use
            http.authenticationProvider(daoAuthenticationProvider());
            /*
                Adds a custom filter authTokenFilter() to the security filter chain.
                 This filter is placed(colocado) before Spring Security's UsernamePasswordAuthenticationFilter, 
                 allowing it to (permitiendole) intercept requests and validate JWT tokens before traditional authentication is performed.
            */
            http.addFilterBefore(authTokenFilter(), org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
