package com.plain.cxf;

import javax.jws.WebService;

/**
 * WebService服务端实现类.
 */
//serviceName指明WSDL中<wsdl:service>与<wsdl:binding>元素的名称,
//endpointInterface属性指向Interface类全称.
@WebService(serviceName = "UserService",
        endpointInterface = "com.plain.cxf.UserService",
        targetNamespace = WsConstants.NS)
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO getUser(String userId) {
        UserDTO dto = new UserDTO();
        dto.setId(Long.parseLong("1001"));
        dto.setLoginName("dongwq");
        dto.setName("张三");
        dto.setEmail("dongwq@qq.com");
        return dto;
    }

    @Override
    public String getUserName(String userId) {
        return "Lincon";
    }
}