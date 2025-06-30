package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.ProductProjection;
import com.commercetools.api.models.product_search.*;
import com.commercetools.api.models.search.*;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ProductPagedSearchResponse>> getProducts(
            String keyword,
            String storeKey,
            Boolean includeFacets) {

        ProductSearchRequestBuilder builder = ProductSearchRequestBuilder.of()
                .withSort(ssb -> ssb
                        .field("variants.prices.centAmount")
                        .mode(SearchSortMode.MIN)
                        .order(SearchSortOrder.ASC)
                        .filter(SearchAndExpressionBuilder.of()
                                .and(
                                    Arrays.asList(SearchExactExpressionBuilder.of().exact(
                                            SearchExactValueBuilder.of()
                                                    .field("variants.prices.currencyCode")
                                                    .fieldType(SearchFieldType.TEXT)
                                                    .value("EUR").build()
                                        ).build(),
                                        SearchExactExpressionBuilder.of().exact(
                                            SearchExactValueBuilder.of()
                                                    .field("variants.prices.country")
                                                    .fieldType(SearchFieldType.TEXT)
                                                    .value("DE").build()
                                        ).build()
                                    )
                                ).build()
                        )
                )
                .productProjectionParameters(pppb -> pppb
                        .priceCountry("DE")
                        .priceCurrency("EUR"))
                .markMatchingVariants(true);

        // Check if at least one is provided, store and/or a keyword
        if (StringUtils.isNotEmpty(keyword) || StringUtils.isNotEmpty(storeKey)) {

            // Check if both are provided, store and a keyword
            if (StringUtils.isNotEmpty(keyword) && StringUtils.isNotEmpty(storeKey)) {

                final String storeId = getStoreId(storeKey);
                builder.query(createSearchQuery(keyword, storeId))
                        .productProjectionParameters(createProductProjectionParams(storeKey));

            } else if (StringUtils.isNotEmpty(keyword)) { // Check if only keyword is provided

                builder.query(createFullTextQuery(keyword)); // TODO 2: Implement createFullTextQuery()

            } else if (StringUtils.isNotEmpty(storeKey)) { // Check if only store is provided
                final String storeId = getStoreId(storeKey);
                builder.query(createStoreQuery(storeId)) // TODO 1: Implement createStoreQuery()
                        .productProjectionParameters(createProductProjectionParams(storeKey)); // TODO 1: Implement createProductProjectionParams()
            }
        }

        if (includeFacets != null && includeFacets){
            builder.facets(createFacets()); // TODO 3: Implement createFacets()
        }

        return apiRoot
                .products()
                .search()
                .post(builder.build())
                .execute();
    }

    private List<ProductSearchFacetExpression> createFacets(){
        throw new NotImplementedException("This method is not implemented yet.");
    }

    private String getStoreId(String storeKey) {
        return apiRoot.stores().withKey(storeKey).get().executeBlocking().getBody().getId();
    }

    private SearchQuery createSearchQuery(String keyword, String storeId) {
        return SearchAndExpressionBuilder.of()
                .and(Arrays.asList(
                        createFullTextQuery(keyword),
                        createStoreQuery(storeId)
                ))
                .build();
    }

    private SearchQuery createFullTextQuery(String keyword) {
        // TODO: Implement to enable keyword search
        throw new NotImplementedException("This method is not implemented yet.");
    }

    private SearchQuery createStoreQuery(String storeId) {
        // TODO: Implement to enable store-specific search
        throw new NotImplementedException("This method is not implemented yet.");
    }

    private ProductSearchProjectionParams createProductProjectionParams(String storeKey) {
        // TODO: Implement to enable storeProjections
        throw new NotImplementedException("This method is not implemented yet.");
    }

    public CompletableFuture<ApiHttpResponse<ProductProjection>> getProductByKey(String productKey) {
        return apiRoot
                .productProjections()
                .withKey(productKey)
                .get()
                .execute();
    }

}
