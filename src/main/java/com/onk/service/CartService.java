package com.onk.service;

import com.onk.core.results.DataResult;
import com.onk.core.results.Result;
import com.onk.model.Product;

import java.util.List;

public interface CartService {

    DataResult<List<Product>> getProductsInCart();

    Result addProductToCart(Long productId);

    Result removeProductFromCart(Long cartId, Long productId);
}
