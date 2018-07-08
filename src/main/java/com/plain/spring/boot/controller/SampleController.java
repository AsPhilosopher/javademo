package com.plain.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SampleController {
    @RequestMapping("/haha")
    public String index() {
        return "index";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}