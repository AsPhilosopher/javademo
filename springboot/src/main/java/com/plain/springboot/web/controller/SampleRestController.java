package com.plain.springboot.web.controller;

import com.plain.springboot.web.dto.request.VUERequest;
import com.plain.springboot.web.dto.response.PlainResponse;
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
