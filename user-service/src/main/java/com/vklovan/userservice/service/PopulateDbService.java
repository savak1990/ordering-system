package com.vklovan.userservice.service;

import com.github.javafaker.Faker;
import com.vklovan.userservice.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

import static org.springframework.data.relational.core.query.Query.empty;

@Service
@Profile(value = {"dev", "staging"})
@DependsOn("initDbService")
@Slf4j
@RequiredArgsConstructor
public class PopulateDbService implements CommandLineRunner {

    private final R2dbcEntityTemplate userEntityTemplate;

    private Faker faker = Faker.instance();

    @Override
    public void run(String... args) throws Exception {
        log.info("Trying to populate DB with test data");

        userEntityTemplate.count(empty(), User.class)
                .flatMapMany(count -> count == 0
                        ? Flux.range(0, 1000)
                        : Flux.error(new RuntimeException("User DB already populated")))
                .map(i -> generateUser())
                .flatMap(userEntityTemplate::insert)
                .subscribe(
                        p -> {},
                        err -> log.info(err.getMessage()),
                        () -> log.info("User DB populated with dev data"));

    }

    private User generateUser() {
        User user = new User();
        user.setName(faker.name().fullName());
        user.setBalance(new BigDecimal(faker.commerce().price(100, 5000)));
        return user;
    }
}
