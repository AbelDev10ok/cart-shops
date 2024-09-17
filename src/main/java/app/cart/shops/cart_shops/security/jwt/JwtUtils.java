package app.cart.shops.cart_shops.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import app.cart.shops.cart_shops.security.user.ShopUserDetail;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/*
    This class is responsible for managing the token
*/
@Component
public class JwtUtils {

    @Value("${auth.token.jwtSecret}")
    private String jwtSecret;
    @Value("${auth.token.expirationInMils}")
    private int expirationTime;

    //this metod generate a token for a user authenticated
    public String generateTokenForUser(Authentication authentication){
        // get the user authenticated with information about the user

        // get the UserDetail object from the authentication object
        ShopUserDetail userPrincipal = (ShopUserDetail) authentication.getPrincipal();
        // extract the roles of the user and convert them to a list of strings
        List<String> roles = userPrincipal.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
        // create the token with the information of the user
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                // information about the user
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                // date of creation and expiration of the token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expirationTime))
                // sign the token with the secret key
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

        // En JwtUtils o donde generes el token
        // public void generateJwtCookie(HttpServletResponse response, String token) {
        //     Cookie cookie = new Cookie("jwt-token", token);
        //     cookie.setHttpOnly(true);
        //     cookie.setSecure(true); // Si usas HTTPS
        //     cookie.setPath("/");
        //     cookie.setMaxAge(expirationTime); // O el tiempo de expiración del token
        //     response.addCookie(cookie);

        // }

    // this metod generate a key for the token
    private Key key(){
        // decode the secret key
        // create a key for the token using  the key decoded
        // return key encoded
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // this metod get the email from the token
    public String getUsernameFromToken(String token){
        // return the email from the token
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // this metod validate the token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);
            return true;
        }catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException  e){
            throw new JwtException(e.getMessage());
        }
    }

}
