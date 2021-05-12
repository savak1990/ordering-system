package com.vklovan.productservice.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@TestConfiguration
public class MongoTestConfig {

    @Value("${spring.data.mongodb.database}")
    String databaseName;

    @Value("${spring.data.mongodb.uri}")
    String uri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients
                .create(uri);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        return new ReactiveMongoTemplate(mongoClient, databaseName);
    }

}
