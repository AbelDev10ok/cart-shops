package app.cart.shops.cart_shops.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private String messae;
    private Object data;

}
