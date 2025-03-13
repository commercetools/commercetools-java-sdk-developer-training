package com.training.handson.services;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.models.common.LocalizedString;
import com.commercetools.importapi.models.common.MoneyBuilder;
import com.commercetools.importapi.models.common.ProductTypeKeyReferenceBuilder;
import com.commercetools.importapi.models.importrequests.ImportResponse;
import com.commercetools.importapi.models.importsummaries.ImportSummary;
import com.commercetools.importapi.models.productdrafts.PriceDraftImportBuilder;
import com.commercetools.importapi.models.productdrafts.ProductDraftImport;
import com.commercetools.importapi.models.productdrafts.ProductDraftImportBuilder;
import com.commercetools.importapi.models.productdrafts.ProductVariantDraftImportBuilder;
import com.commercetools.importapi.models.products.ProductImport;
import com.commercetools.importapi.models.products.ProductImportBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ImportService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ImportResponse>> importProductsFromCsv(
            final MultipartFile csvFile) {

        // TODO: Import products from the csv file
        // TODO: Update and use getProductImportsFromCsv method to parse the csv file
        return CompletableFuture.completedFuture(
                new ApiHttpResponse<>(501, null, ImportResponse.of())
        );
    }

    private List<ProductDraftImport> getProductDraftImportsFromCsv(final MultipartFile csvFile) {

        List<ProductDraftImport> productImports = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(","));
                if (values.size() >= 4) {
                    LocalizedString name = LocalizedString.of();
                    name.setValue("en-US", values.get(2));
                    LocalizedString slug = LocalizedString.of();
                    slug.setValue("en-US", values.get(3));
                    ProductDraftImport productImport = ProductDraftImportBuilder.of()
                            .key(values.get(0))
                            .productType(ProductTypeKeyReferenceBuilder.of().key(values.get(1)).build())
                            .name(name)
                            .slug(slug)
                            .masterVariant(ProductVariantDraftImportBuilder.of()
                                    .sku(values.get(4))
                                    .key(values.get(4))
                                    .prices(PriceDraftImportBuilder.of()
                                            .key(values.get(4)+"-"+values.get(5).toLowerCase()+"-price")
                                            .value(MoneyBuilder.of()
                                                    .currencyCode(values.get(5))
                                                    .centAmount((long) (Double.parseDouble(values.get(6)) * 100))
                                                    .build())
                                            .build())
                                    .build())
                            .build();
                    productImports.add(productImport);
                }
                else {
                    System.out.println("skipping line");
                }
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
        return productImports;
    }

    public CompletableFuture<ApiHttpResponse<ImportSummary>> getImportContainerSummary(final String containerKey) {
        return
                apiRoot
                        .importContainers()
                        .withImportContainerKeyValue("my-import-container")
                        .importSummaries()
                        .get()
                        .execute();
    }


}
