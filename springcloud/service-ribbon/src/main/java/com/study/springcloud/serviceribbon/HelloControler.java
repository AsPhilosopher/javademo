package com.study.springcloud.serviceribbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA
 * Date: 2017/11/3
 * Time: 上午10:07
 *
 * @author 陈樟杰
 */
@RestController
public class HelloControler {

    @Autowired
    HelloService helloService;

    @RequestMapping(value = "/ribbon")
    public String hi(@RequestParam String name) {
        return helloService.hiService(name) + " Ribbon";
    }


}
