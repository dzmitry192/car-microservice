package com.innowise.carmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CarMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarMicroserviceApplication.class, args);
    }
}
