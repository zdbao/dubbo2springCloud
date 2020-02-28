package com.example.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo(scanBasePackages = {"com.example.provider.service"})
public class DemoDubboProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoDubboProviderApplication.class, args);
    }

}
