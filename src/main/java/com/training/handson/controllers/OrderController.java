package com.training.handson.controllers;

import com.commercetools.api.models.order.Order;
import com.training.handson.dto.CustomFieldRequest;
import com.training.handson.dto.OrderRequest;
import com.training.handson.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/orders/")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<Order>> getOrder(
            @PathVariable String storeKey,
            @PathVariable String id) {
        return orderService.getOrderById(storeKey, id).thenApply(ResponseConverter::convert);
    }

    @PostMapping()
    public CompletableFuture<ResponseEntity<Order>> createOrder(
            @PathVariable String storeKey,
            @RequestBody OrderRequest orderRequest) {

        return orderService.createOrder(storeKey, orderRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("{orderNumber}/custom-fields")
    public CompletableFuture<ResponseEntity<Order>> createCustomFields(
            @PathVariable String storeKey,
            @PathVariable String orderNumber,
            @RequestBody CustomFieldRequest customFieldRequest) {

            return orderService.setCustomFields(storeKey, orderNumber, customFieldRequest).thenApply(ResponseConverter::convert);

    }
}
