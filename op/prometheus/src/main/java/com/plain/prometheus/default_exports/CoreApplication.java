package com.plain.prometheus.default_exports;

import io.prometheus.client.hotspot.DefaultExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnablePrometheusEndpoint
public class CoreApplication extends WebMvcConfigurerAdapter implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CoreApplication.class);
        springApplication.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        DefaultExports.initialize();
    }
}