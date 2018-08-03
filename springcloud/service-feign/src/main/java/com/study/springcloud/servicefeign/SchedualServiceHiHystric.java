package com.study.springcloud.servicefeign;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA
 * Date: 2017/11/3
 * Time: 上午11:38
 *
 * @author 陈樟杰
 */
@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry " + name;
    }
}
