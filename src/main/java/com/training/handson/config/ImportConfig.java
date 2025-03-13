package com.training.handson.config;

import com.commercetools.importapi.client.ProjectApiRoot;
import com.commercetools.importapi.defaultconfig.ImportApiRootBuilder;
import com.commercetools.importapi.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportConfig {
    @Value("${import.clientId}")
    private String clientId;

    @Value("${import.clientSecret}")
    private String clientSecret;

    @Value("${import.scopes}")
    private String scopes;

    @Value("${import.projectKey}")
    private String projectKey;

    @Bean
    public ProjectApiRoot importApiRoot() {
        return ImportApiRootBuilder.of()
                .defaultClient(
                        ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .withScopes(scopes)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1
                )
                .build(projectKey);
    }
}