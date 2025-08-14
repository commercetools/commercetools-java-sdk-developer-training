package com.training.handson.controllers;

import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.type.Type;
import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.training.handson.dto.CustomObjectRequest;
import com.training.handson.services.ExtensionsService;
import com.training.handson.services.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/extensions/")
public class ExtensionsController {

    private final ExtensionsService extensionsService;

    public ExtensionsController(ExtensionsService extensionsService) {
        this.extensionsService = extensionsService;
    }

    @PostMapping("types")
    public CompletableFuture<ResponseEntity<Type>> createType() {

        return extensionsService.createType().thenApply(ResponseConverter::convert);
    }

    @PostMapping("custom-objects")
    public CompletableFuture<ResponseEntity<CustomObject>> createCustomObject(@RequestBody CustomObjectRequest customObjectRequest) {

        return extensionsService.createCustomObject(customObjectRequest).thenApply(ResponseConverter::convert);
    }

    @PostMapping("custom-objects/{container}/{key}")
    public CompletableFuture<ResponseEntity<CustomObject>> appendToCustomObject(@PathVariable String container,
                                                                              @PathVariable String key,
                                                                              @RequestBody Map<String, Object> jsonObject) {

        return extensionsService.appendToCustomObject(container, key, jsonObject).thenApply(ResponseConverter::convert);
    }

    @GetMapping("custom-objects/{container}/{key}")
    public CompletableFuture<ResponseEntity<CustomObject>> getCustomObject(@PathVariable String container,
                                                                           @PathVariable String key) {

        return extensionsService.getCustomObjectWithContainerAndKey(container, key).thenApply(ResponseConverter::convert);
    }
}

