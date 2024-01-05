package com.onk.api;

import com.onk.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-product")
    public ResponseEntity<?> addProductToCart(@RequestParam("product_id") Long productId){
        return ResponseEntity.ok(this.cartService.addProductToCart(productId));
    }
    @PostMapping("/delete-product")
    public ResponseEntity<?> removeProductFromCart(@RequestParam("cart_id") Long cartId, @RequestParam("product_id") Long productId){
        return ResponseEntity.ok(this.cartService.removeProductFromCart(cartId, productId));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductsInCart(){
        return ResponseEntity.ok(this.cartService.getProductsInCart());
    }

}
