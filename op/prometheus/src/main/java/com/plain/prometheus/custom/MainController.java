package com.plain.prometheus.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
public class MainController {

    @Autowired
    CustomMetric customMetric;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String home(HttpServletRequest request) {
        customMetric.processRequest(request.getMethod());
        return "Hello world!";
    }

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public void receive(HttpServletRequest request) throws IOException {
        System.out.println(request.getHeader("content-type"));
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader;
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        }
        System.out.println(stringBuilder);
    }
}
