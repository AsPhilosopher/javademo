package com.plain.cxf.jax_rs.client;

import com.plain.cxf.UserDTO;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

public class ClientByHttp {
    public static void main(String[] args) {
        WebClient client = WebClient.create("http://localhost:8080/cxf/jaxrs");
        String format = MediaType.APPLICATION_XML;
        client.path("/user/15811006.xml").accept(format).type(format);
        UserDTO user = client.get(UserDTO.class);
        System.out.println("userName: " + user.getName());
    }
}
