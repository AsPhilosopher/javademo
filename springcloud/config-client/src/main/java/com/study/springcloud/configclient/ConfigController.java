package com.study.springcloud.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA
 * Date: 2017/11/8
 * Time: 下午5:49
 *
 * @author 陈樟杰
 */
@RestController
public class ConfigController {
    @Value("${foo}")
    private String foo;

    @RequestMapping(value = "/hi")
    public String hi() {
        return foo;
    }
}
