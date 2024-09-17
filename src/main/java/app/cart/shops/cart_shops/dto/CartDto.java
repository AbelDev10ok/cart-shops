package app.cart.shops.cart_shops.dto;

import java.util.Set;
import java.util.HashSet;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartDto {
    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}
