package com.training.handson.controllers;

import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.training.handson.services.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

