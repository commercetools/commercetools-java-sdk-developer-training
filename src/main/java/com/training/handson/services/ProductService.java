package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.product.ProductProjection;
import com.commercetools.api.models.product_search.ProductSearchFacetCountLevelEnum;
import com.commercetools.api.models.product_search.ProductSearchFacetDistinctExpressionBuilder;
import com.commercetools.api.models.product_search.ProductSearchFacetExpression;
import com.commercetools.api.models.product_search.ProductSearchFacetScopeEnum;
import com.commercetools.api.models.search.*;
import com.commercetools.graphql.api.GraphQL;
import com.commercetools.graphql.api.GraphQLRequest;
import com.commercetools.graphql.api.GraphQLResponse;
import com.training.handson.dto.SearchRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<ProductProjection>> getProductByKey(String productKey) {
        return apiRoot
            .productProjections()
            .withKey(productKey)
            .get()
            .execute();
    }

    public CompletableFuture<ApiHttpResponse<GraphQLResponse<Object>>> getProducts(SearchRequest searchRequest) {

        // Build GraphQL query (mirrors the JS implementation)
        String query = """
            query ProductsSearch($query: SearchQueryInput, $facets: [ProductSearchFacetExpressionInput!], $sort: [SearchSortingInput!], $markMatchingVariants: Boolean, $locale: Locale!, $currency: Currency!, $country: Country!) {
              productsSearch(query: $query, facets: $facets, sort: $sort, markMatchingVariants: $markMatchingVariants) {
                total
                offset
                limit
                results {
                  id
                  product {
                    id
                    key
                    masterData {
                      current {
                        name(locale: $locale)
                        slug(locale: $locale)
                        masterVariant {
                          sku
                          images { url }
                          price(currency: $currency, country: $country) { value { __typename centAmount currencyCode } discounted { value { __typename centAmount currencyCode } } }
                          attributesRaw { name value }
                        }
                      }
                    }
                  }
                }
                facets {
                  __typename
                  ... on ProductSearchFacetResultBucket {
                    name
                    buckets { count key }
                  }
                }
              }
            }
            """;

        // Build variables
        Map<String, Object> variables = new HashMap<>();

        // query variable (fullText and/or store) using SDK SearchQuery
        SearchQuery searchQuery = buildSearchQueryInput(searchRequest);
        if (searchQuery != null) {
            variables.put("query", searchQuery);
        }

        // facets
        if (searchRequest.isFacets()) {
            variables.put("facets", createFacetsInput(searchRequest.getLocale()));
        }

        // sort
        variables.put("sort", createSortInput(searchRequest));

        variables.put("markMatchingVariants", true);
        variables.put("locale", searchRequest.getLocale());
        variables.put("currency", searchRequest.getCurrency());
        variables.put("country", searchRequest.getCountry());

        GraphQLRequest<Object> graphQLRequest = GraphQL
            .query(query)
            .variables(builder -> {
                variables.forEach(builder::addValue);
                return builder;
            })
            .dataMapper(data -> (Object) data)
            .build();

        return apiRoot
            .graphql()
            .query(graphQLRequest)
            .execute();
    }

    

    private List<ProductSearchFacetExpression> createFacetsInput(String locale) {
        List<ProductSearchFacetExpression> facets = new ArrayList<>();

        ProductSearchFacetExpression colorDistinct = ProductSearchFacetDistinctExpressionBuilder.of()
                .distinct(d -> d
                        .name("Color")
                        .field("variants.attributes.search-color.label")
                        .fieldType(SearchFieldType.LENUM)
                        .language(locale)
                        .level(ProductSearchFacetCountLevelEnum.VARIANTS)
                        .scope(ProductSearchFacetScopeEnum.QUERY)
                )
                .build();

        ProductSearchFacetExpression finishDistinct = ProductSearchFacetDistinctExpressionBuilder.of()
                .distinct(d -> d
                        .name("Finish")
                        .field("variants.attributes.search-finish.label")
                        .fieldType(SearchFieldType.LENUM)
                        .language(locale)
                        .level(ProductSearchFacetCountLevelEnum.VARIANTS)
                        .scope(ProductSearchFacetScopeEnum.QUERY)
                )
                .build();

        facets.add(colorDistinct);
        facets.add(finishDistinct);
        return facets;
    }

    private String getStoreId(String storeKey) {
        return apiRoot.stores().withKey(storeKey).get().executeBlocking().getBody().getId();
    }

    

    private SearchQuery buildSearchQueryInput(SearchRequest searchRequest) {
        boolean hasKeyword = StringUtils.isNotEmpty(searchRequest.getKeyword());
        boolean hasStoreKey = StringUtils.isNotEmpty(searchRequest.getStoreKey());

        if (!hasKeyword && !hasStoreKey) return null;

        com.commercetools.api.models.search.SearchQuery fullTextExpr = null;
        com.commercetools.api.models.search.SearchQuery storeExpr = null;

        if (hasStoreKey) {
            final String storeId = getStoreId(searchRequest.getStoreKey());
            storeExpr = SearchExactExpressionBuilder.of()
              .exact(v -> v.field("stores").value(storeId).fieldType(SearchFieldType.SET_REFERENCE))
              .build();
        }

        if (hasKeyword) {
            fullTextExpr = com.commercetools.api.models.search.SearchFullTextExpressionBuilder.of()
                    .fullText(v -> v.field("name").language(searchRequest.getLocale()).value(searchRequest.getKeyword()).mustMatch(SearchMatchType.ANY))
                    .build();
        }

        if (storeExpr != null && fullTextExpr != null) {
            return SearchAndExpressionBuilder.of().and(List.of(fullTextExpr, storeExpr)).build();
        }

        return storeExpr != null ? (SearchQuery) storeExpr : fullTextExpr;
    }

    private List<SearchSorting> createSortInput(SearchRequest request) {
        SearchQuery filter = SearchAndExpressionBuilder.of()
          .and(List.of(
            SearchExactExpressionBuilder.of().exact(v -> v.field("variants.prices.country").value(request.getCountry())).build(),
            SearchExactExpressionBuilder.of().exact(v -> v.field("variants.prices.currencyCode").value(request.getCurrency())).build()
          ))
          .build();

        SearchSorting sort = SearchSortingBuilder.of()
          .field("variants.prices.centAmount")
          .order(SearchSortOrder.ASC)
          .mode(SearchSortMode.MIN)
          .filter(filter)
          .build();

        return List.of(sort);
    }

}
