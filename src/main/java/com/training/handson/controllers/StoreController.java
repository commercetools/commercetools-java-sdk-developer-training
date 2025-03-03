package com.training.handson.controllers;

import com.commercetools.api.models.store.Store;
import com.commercetools.api.models.store.StorePagedQueryResponse;
import com.training.handson.services.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/{storeId}")
    public CompletableFuture<ResponseEntity<Store>> getStore(@PathVariable String storeId) {
        return storeService.getStoreById(storeId).thenApply(ResponseConverter::convert);
    }

    @GetMapping()
    public CompletableFuture<ResponseEntity<StorePagedQueryResponse>> getStores() {
        return storeService.getStores().thenApply(ResponseConverter::convert);
    }

}
