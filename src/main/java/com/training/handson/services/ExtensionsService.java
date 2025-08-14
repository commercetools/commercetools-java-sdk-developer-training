package com.training.handson.services;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.models.custom_object.CustomObject;
import com.commercetools.api.models.type.*;
import com.training.handson.dto.CustomObjectRequest;
import io.vrap.rmf.base.client.ApiHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class ExtensionsService {

    @Autowired
    private ProjectApiRoot apiRoot;

    public CompletableFuture<ApiHttpResponse<Type>> createType() {
        // Define labels for the fields
        Map<String, String> labelsForFieldInstructions = new HashMap<String, String>() {{
            put("de-DE", "Instructions");
            put("en-US", "Instructions");
        }};
        Map<String, String> labelsForFieldTime = new HashMap<String, String>() {{
            put("de-DE", "Preferred Time");
            put("en-US", "Preferred Time");
        }};

        // Define the fields
        List<FieldDefinition> definitions = Arrays.asList(
                FieldDefinitionBuilder.of()
                        .name("instructions")
                        .required(false)
                        .label(lsb -> lsb.values(labelsForFieldInstructions))
                        .type(CustomFieldStringType.of())
                        .build(),
                FieldDefinitionBuilder.of()
                        .name("time")
                        .required(false)
                        .label(lsb -> lsb.values(labelsForFieldTime))
                        .type(CustomFieldStringType.of())
                        .build()
        );

        // Define the name for the type
        Map<String, String> nameForType = new HashMap<String, String>() {{
            put("de-DE", "CT Delivery instructions");
            put("en-US", "CT Delivery instructions");
        }};

        // Create the custom type asynchronously
        return apiRoot
                .types()
            .post(
                typeDraftBuilder -> typeDraftBuilder
                    .key("ct-delivery-instructions")
                    .name(lsb -> lsb.values(nameForType))
                    .resourceTypeIds(
                        ResourceTypeId.CUSTOMER,
                        ResourceTypeId.ORDER,
                        ResourceTypeId.ADDRESS
                    )
                    .fieldDefinitions(definitions)
            ).execute();
    }

    public CompletableFuture<Boolean> existsCustomObjectWithContainerAndKey(
            final String container,
            final String key) {

        return apiRoot
                .customObjects()
                .head()
                .withWhere("container=\""+container+"\"")
                .addWhere("key=\""+key+"\"")
                .execute()
                .thenApply(response -> response.getStatusCode() == 200)
                .exceptionally(ex -> false);
    }

    public CompletableFuture<ApiHttpResponse<CustomObject>> createCustomObject(
            final CustomObjectRequest customObjectRequest) {

        Map<String, Object> jsonObject = new HashMap<>();
        String container = customObjectRequest.getContainer();
        String key = customObjectRequest.getKey();

        return existsCustomObjectWithContainerAndKey(container, key)
                .thenCompose(exists -> {
                    if (!exists) {
                        System.out.println("Creating custom object: " + container + " / " + key);
                        return apiRoot.customObjects()
                                .post(draftBuilder -> draftBuilder
                                        .container(container)
                                        .key(key)
                                        .value(jsonObject))
                                .execute();
                    } else {
                        System.out.println("Custom object already exists: " + container + " / " + key);
                        return getCustomObjectWithContainerAndKey(container, key);
                    }
                });
    }

    public CompletableFuture<ApiHttpResponse<CustomObject>> getCustomObjectWithContainerAndKey(
            final String container,
            final String key) {

        return apiRoot
                .customObjects()
                .withContainerAndKey(container, key)
                .get()
                .execute();
    }

    public CompletableFuture<ApiHttpResponse<CustomObject>> appendToCustomObject(
            final String container,
            final String key,
            final Map<String, Object> jsonObject) {

        return getCustomObjectWithContainerAndKey(container, key)
                .thenCompose(customObjectResponse -> {
                    Map<String, Object> currentSubscribers = (Map<String, Object>) customObjectResponse.getBody().getValue();
                    currentSubscribers.putAll(jsonObject);

                    return apiRoot.customObjects()
                            .post(customObjectDraftBuilder -> customObjectDraftBuilder
                                    .container(container)
                                    .key(key)
                                    .value(currentSubscribers))
                            .execute();
                });
    }

}
