package com.plain.resilience4j.service;

import java.util.concurrent.TimeUnit;

public class BackendService {
    public String doSomething() {
//        int a = 10 / 0;
        /*try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return "do some thing";
    }
}
