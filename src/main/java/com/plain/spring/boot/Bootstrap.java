package com.plain.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author czj
 */
@SpringBootApplication
public class Bootstrap {
    /*@Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.getUrlMappings().clear();
        registration.addUrlMappings("/*");
        return registration;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }
}
