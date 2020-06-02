package com.plain.springboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.TimeUnit;

@Controller
public class SampleController {
    @RequestMapping("/haha")
    public String index() throws InterruptedException {
        System.out.println("IN-IN");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("OVER-OVER");
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}