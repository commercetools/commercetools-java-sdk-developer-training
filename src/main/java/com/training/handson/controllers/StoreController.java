package com.training.handson.controllers;

import com.commercetools.api.models.store.Store;
import com.commercetools.api.models.store.StorePagedQueryResponse;
import com.training.handson.services.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<Store>> getStore(@PathVariable String id) {
        return storeService.getStoreById(id).thenApply(ResponseConverter::convert);
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<StorePagedQueryResponse>> getStores() {
        return storeService.getStores().thenApply(ResponseConverter::convert);
    }

}
