package com.vsquad.iroas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IroasApplication {

    public static void main(String[] args) {
        SpringApplication.run(IroasApplication.class, args);
    }

}
