package com.apiboad6.reactjwttokenproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ReactJwtTokenProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactJwtTokenProjectApplication.class, args);
    }

}
