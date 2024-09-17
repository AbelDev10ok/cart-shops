package app.cart.shops.cart_shops.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import app.cart.shops.cart_shops.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
// Suggested code may be subject to a license. Learn more: ~LicenseLog:4153361381.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleException(AccessDeniedException ex) {
        String message = "You do not have permission to this action";
        return new ResponseEntity<>(message,HttpStatus.FORBIDDEN);
    }
}
