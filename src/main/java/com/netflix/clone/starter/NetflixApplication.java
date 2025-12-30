package com.netflix.clone.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "com.netflix.clone")
@EntityScan(basePackages = "com.netflix.clone")
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
@EnableCaching
public class NetflixApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetflixApplication.class, args);
    }

}
