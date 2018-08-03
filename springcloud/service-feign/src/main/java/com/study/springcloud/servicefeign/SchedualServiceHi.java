package com.study.springcloud.servicefeign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA
 * Date: 2017/11/3
 * Time: 上午11:01
 *
 * @author 陈樟杰
 * <p>
 * value与name一样 是调用的service的名称
 */
@FeignClient(value = "service-hi", fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {
    /**
     * service对外暴露的接口
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
