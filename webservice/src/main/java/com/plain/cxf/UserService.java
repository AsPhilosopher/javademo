package com.plain.cxf;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * JAX-WS2.0的WebService接口定义类 *
 * 使用JAX-WS2.0 annotation设置WSDL中的定义.
 * 使用WSResult及其子类包裹返回结果.
 * 使用DTO传输对象隔绝系统内部领域对象的修改对外系统的影响.
 */
//name 指明wsdl中<wsdl:portType>元素的名称
@WebService(name = "UserService", targetNamespace = WsConstants.NS)
public interface UserService {

    //@WebService是必须的；@WebParam不是必须的。
    //如果没有@WebParam的描述，在wsdl文件内描述的方法中，参数名将变为arg0,arg1…以此类推.
    String getUserName(@WebParam(name = "userId") String userId);

    UserDTO getUser(@WebParam(name = "userId") String userId);
}
