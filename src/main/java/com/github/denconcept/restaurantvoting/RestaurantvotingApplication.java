package com.github.denconcept.restaurantvoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class RestaurantvotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantvotingApplication.class, args);
    }
}