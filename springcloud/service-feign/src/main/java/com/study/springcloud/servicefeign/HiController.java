package com.study.springcloud.servicefeign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA
 * Date: 2017/11/3
 * Time: 上午11:02
 *
 * @author 陈樟杰
 */
@RestController
public class HiController {

    @Autowired
    SchedualServiceHi schedualServiceHi;

    @RequestMapping(value = "/feign", method = RequestMethod.GET)
    public String sayHi(@RequestParam String name) {
        return schedualServiceHi.sayHiFromClientOne(name) + " Feign";
    }
}

