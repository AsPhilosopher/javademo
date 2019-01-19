package com.plain.prometheus.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class MainController {

    @Autowired
    CustomMetric customMetric;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(HttpServletRequest request, HttpServletResponse response) {
        customMetric.processRequest(request.getMethod());
        return "Hello world!";
    }
}
