package com.plain.cxf.client;

import com.plain.cxf.UserDTO;
import com.plain.cxf.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * 通过代理API调用，依赖于服务端的接口
 */
public class TestClientByProxy {
    public static void main(String[] args) {
        // 调用WebService
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(UserService.class);
        factory.setAddress("http://localhost:8080/cxf/soap/userService");
        UserService service = (UserService) factory.create();
        UserDTO user = service.getUser("1001");
        System.out.println(user);
    }
}
