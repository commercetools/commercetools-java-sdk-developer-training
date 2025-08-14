package com.training.handson.controllers;

import com.commercetools.graphql.api.GraphQLResponse;
import com.commercetools.graphql.api.types.OrderQueryResult;
import com.training.handson.services.GraphqlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/graphql/")
public class GraphqlController {

    private final GraphqlService graphqlService;

    public GraphqlController(GraphqlService graphqlService) {
        this.graphqlService = graphqlService;
    }

    @GetMapping("orders")
    public CompletableFuture<ResponseEntity<GraphQLResponse<OrderQueryResult>>> getOrders(
            @RequestParam String email) {
        return graphqlService.getOrdersByEmail(email).thenApply(ResponseConverter::convert);
    }

}
