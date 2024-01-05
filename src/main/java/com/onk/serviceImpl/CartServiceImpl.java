package com.onk.serviceImpl;

import com.onk.component.MessageService;
import com.onk.core.results.*;
import com.onk.dto.CartDto;
import com.onk.model.Cart;
import com.onk.model.Product;
import com.onk.repository.CartRepository;
import com.onk.repository.ProductRepository;
import com.onk.security.auth.SecurityManager;
import com.onk.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final MessageService messageService;


    @Override
    public DataResult<List<Product>> getProductsInCart() {
        try{
            var user = SecurityManager.getCurrentUser();
            assert user != null;
            Cart cart = user.getCart();
            if(cart == null){
                return new ErrorDataResult<>(
                        "Sepet bulunamadı"
                );
            }
            return new SuccessDataResult<>(
                    cart.getProducts(),
                    "Sepetteki ürünler listelendi."
            );
        }catch (Exception e){
            return new ErrorDataResult<>(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result addProductToCart(Long productId) {
        try{
            var user = SecurityManager.getCurrentUser();
            assert user != null;
            Cart cart = user.getCart();
            if(cart == null){
                cart = Cart.builder()
                        .user(user)
                        .products(new ArrayList<>())
                        .build();
            }
            Product product = productRepository.findById(productId).orElse(null);
            if(product == null){
                return new ErrorResult(
                        "ürün bulunamadı! Null olmamalı."
                );
            }
            List<Product> products = cart.getProducts();
            products.add(product);
            System.out.println(products.size());
            cart.setProducts(products);
            cartRepository.save(cart);
            return new SuccessResult(
                    "Sepete ürün başarıyla eklendi"
            );
        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }

    @Override
    public Result removeProductFromCart(Long cartId, Long productId) {
        try{
            var cart = cartRepository.findById(cartId).orElse(null);
            Product product = productRepository.findById(productId).orElse(null);
            if(cart == null || product == null){
                return new ErrorResult(
                        "Sepet veya ürün bulunamadı! Null olmamalı."
                );
            }
            List<Product> products = cart.getProducts();
            products.remove(product);
            cart.setProducts(products);
            cartRepository.save(cart);
            return new SuccessResult(
                    "Sepetten ürün başarıyla kaldırıldı."
            );
        }catch(Exception e){
            return new ErrorResult(
                    messageService.getMessage("error.message", null)
            );
        }
    }


}
