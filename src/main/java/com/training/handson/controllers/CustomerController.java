package com.training.handson.controllers;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.training.handson.dto.CustomerCreateRequest;
import com.training.handson.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/in-store/{storeKey}/customers/")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping()
    public CompletableFuture<ResponseEntity<CustomerSignInResult>> createCustomer(
            @PathVariable String storeKey,
            @RequestBody CustomerCreateRequest customerCreateRequest) {

        return customerService.createCustomer(storeKey, customerCreateRequest).thenApply(ResponseConverter::convert);
    }

    @GetMapping("{id}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerById(
            @PathVariable String storeKey,
            @PathVariable String id) {
        return customerService.getCustomerById(storeKey, id).thenApply(ResponseConverter::convert);
    }

    @GetMapping("key={key}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerByKey(
            @PathVariable String storeKey,
            @PathVariable String key) {
        return customerService.getCustomerByKey(storeKey, key).thenApply(ResponseConverter::convert);
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<CustomerSignInResult>> loginCustomer(
            @PathVariable String storeKey,
            @RequestBody CustomerCreateRequest customerCreateRequest) {

        return customerService.loginCustomer(storeKey, customerCreateRequest).thenApply(ResponseConverter::convert);
    }
}
