package com.training.handson.controllers;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.training.handson.dto.CustomerCreateRequest;
import com.training.handson.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CustomerSignInResult>> createCustomer(
            @RequestBody CustomerCreateRequest customerCreateRequest) {

        return customerService.createCustomer(customerCreateRequest).thenApply(ResponseConverter::convert);
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id).thenApply(ResponseConverter::convert);
    }

    @GetMapping("/key={key}")
    public CompletableFuture<ResponseEntity<Customer>> getCustomerByKey(
            @PathVariable String key) {
        return customerService.getCustomerByKey(key).thenApply(ResponseConverter::convert);
    }

    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<CustomerSignInResult>> loginCustomer(
            @RequestBody CustomerCreateRequest customerCreateRequest) {

        return customerService.loginCustomer(customerCreateRequest).thenApply(ResponseConverter::convert);
    }
}
