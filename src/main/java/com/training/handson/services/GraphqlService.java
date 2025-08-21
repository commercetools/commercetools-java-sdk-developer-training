package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.graphql.api.GraphQL;
import com.commercetools.graphql.api.GraphQLData;
import com.commercetools.graphql.api.GraphQLRequest;
import com.commercetools.graphql.api.GraphQLResponse;
import com.commercetools.graphql.api.types.OrderQueryResult;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GraphqlService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<GraphQLResponse<OrderQueryResult>>> getOrdersByEmail(
            final String customerEmail,
            final String locale) {

        String query = """
            query($where:String!, $locale:Locale!) {
              orders(where: $where) {
                results {
                  customerEmail
                  customer {
                    firstName
                    lastName
                  }
                  lineItems {
                    name(locale: $locale)
                    totalPrice {centAmount currencyCode}
                  }
                  CartTotal: totalPrice {
                    centAmount
                    currencyCode
                  }
                }
              }
            }
            """;




        // Create the GraphQL request
        GraphQLRequest<OrderQueryResult> graphQLRequest = GraphQL
                .query(query)
                .variables(builder -> builder.addValue("where", "customerEmail=\""+customerEmail+"\"").addValue("locale", locale))
                .dataMapper(GraphQLData::getOrders)
                .build();

        // Execute the query
        return apiRoot
                .graphql()
                .query(graphQLRequest)
                .execute();
    }

}
