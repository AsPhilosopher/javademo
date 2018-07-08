package com.plain.spring.boot.controller;

import com.plain.spring.boot.dto.request.VUERequest;
import com.plain.spring.boot.dto.response.PlainResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleRestController {
    @RequestMapping("/mytest")
    public PlainResponse vueRequest(@RequestBody VUERequest vueRequest) {
        PlainResponse plainResponse = new PlainResponse();
        plainResponse.setData(vueRequest);
        return plainResponse;
    }
}
