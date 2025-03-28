package com.training.handson.controllers;

import com.commercetools.api.models.cart.Cart;
import com.training.handson.dto.AddressRequest;
import com.training.handson.dto.CartUpdateRequest;
import com.training.handson.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Cart>> getCart(@PathVariable String id) {
        return cartService.getCartById(id).thenApply(ResponseConverter::convert);
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<Cart>> updateCart(
            @RequestBody CartUpdateRequest cartUpdateRequest) {

        return cartService.updateCart(cartUpdateRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("/discount-code")
    public CompletableFuture<ResponseEntity<Cart>> addDiscountCode(
            @RequestBody CartUpdateRequest cartUpdateRequest) {

        return cartService.addDiscountToCart(cartUpdateRequest.getCartId(), cartUpdateRequest.getCode()).thenApply(ResponseConverter::convert);
    }

    @PostMapping("/shipping-address")
    public CompletableFuture<ResponseEntity<Cart>> setShippingAddress(
            @RequestBody AddressRequest addressRequest) {

            return cartService.setShippingAddress(addressRequest).thenApply(ResponseConverter::convert);
    }

}
