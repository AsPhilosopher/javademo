package com.plain.cxf.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * 不依赖服务端的接口
 *
 * @throws Exception
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        //不依赖服务器端接口来完成调用的，也就是不仅仅能调用Java的接口
        JaxWsDynamicClientFactory clientFactory =
                JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(
                "http://localhost:8080/cxf/soap/userService?wsdl");
        Object[] result = client.invoke("getUserName", "1001");
        System.out.println(result[0]);
    }
}
