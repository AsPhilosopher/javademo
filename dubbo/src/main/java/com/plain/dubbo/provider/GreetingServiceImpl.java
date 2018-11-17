package com.plain.dubbo.provider;

import com.plain.dubbo.service.GreetingService;

public class GreetingServiceImpl implements GreetingService {
    public String sayHello(String name) {
        System.out.println("SUCCESS");
        return "Hello " + name;
    }
}
