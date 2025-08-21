package com.training.handson.controllers;

import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.type.Type;
import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.training.handson.dto.CustomObjectRequest;
import com.training.handson.services.ExtensionsService;
import com.training.handson.services.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/imports/")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("products")
    public CompletableFuture<ResponseEntity<ImportResponse>> importProducts(@RequestParam("file") MultipartFile file) {
            return importService.importProductsFromCsv(file).thenApply(ResponseConverter::convert);
    }

}

