package app.cart.shops.cart_shops.security.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import app.cart.shops.cart_shops.security.user.ShopUserDetail;
import app.cart.shops.cart_shops.security.user.ShopUserDetailService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 OncePerRequest
 which guarantees(lo que garantiza) that the filter will be executed only once(una vez) per request.
 
In summary: AuthTokenFilter intercepts the requests, extracts the JWT token, validates it and, if valid,
authenticates the user to Spring Security. 
This allows(permite) controllers and other components of your application to access the authenticated user's information.
 */

public class AuthTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private ShopUserDetailService shopUserDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Extracts the JWT token from the Authorization header of the request.
        String jwt = parseJwt(request);
        try {
            // If a token is found, it is validated using jwtUtils.validateToken(jwt).
            if(StringUtils.hasText(jwt) && jwt != null && jwtUtils.validateToken(jwt)){
                String username = jwtUtils.getUsernameFromToken(jwt);
                // Get user information using shopUserDetailService.loadUserByUsername(username).
                UserDetails shopUserDetail = shopUserDetailService.loadUserByUsername(username);
                // A UsernamePasswordAuthenticationToken object is created with the user's information and roles.
                Authentication authentication = new UsernamePasswordAuthenticationToken(shopUserDetail, null, shopUserDetail.getAuthorities());
                // User authentication is established in the Spring Security security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // The shopUserDetail object is added as(como) an attribute to the request so that it is available(disponible) in the controllers.
                request.setAttribute("shopUserDetail", shopUserDetail);
            }
            
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage() + " : Invalid or expired token, you any login and try again!");
            return;
        }catch(Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(e.getMessage());
            return;
        }
        // the filter chain continues
        filterChain.doFilter(request, response);
    }


    private String parseJwt(HttpServletRequest request){
        String headerAuth = request.getHeader("Authorization");

        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

        

}
