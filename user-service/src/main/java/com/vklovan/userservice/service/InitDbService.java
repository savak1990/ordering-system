package com.vklovan.userservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class InitDbService implements CommandLineRunner {

    @Value("classpath:init.sql")
    private Resource initSql;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Trying to create users and user_transaction tables");
        String query = StreamUtils.copyToString(initSql.getInputStream(), StandardCharsets.UTF_8);

        log.info("Query to execute: {}", query);
        r2dbcEntityTemplate
                .getDatabaseClient()
                .sql(query)
                .then()
                .subscribe(
                        r -> {},
                        err -> log.info("Error during table creation: {}", err.getMessage()),
                        () -> log.info("Tables created"));
    }
}
