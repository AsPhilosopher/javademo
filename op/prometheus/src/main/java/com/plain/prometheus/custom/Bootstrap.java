package com.plain.prometheus.custom;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnablePrometheusEndpoint
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Bootstrap.class);
        springApplication.run(args);
    }
}
