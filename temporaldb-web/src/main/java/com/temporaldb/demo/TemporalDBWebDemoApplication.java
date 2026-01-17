package com.temporaldb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.temporaldb.TemporalDBServer;

/**
 * Spring Boot Application for TemporalDB Web Demo.
 * Demonstrates a Product Inventory System with temporal data tracking.
 */
@SpringBootApplication
public class TemporalDBWebDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemporalDBWebDemoApplication.class, args);
    }

    /**
     * Create TemporalDB server as a singleton bean.
     */
    @Bean
    public TemporalDBServer temporalDBServer() {
        TemporalDBServer server = new TemporalDBServer();
        server.start();
        return server;
    }
}