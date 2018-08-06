package com.plain.cxf.jax_rs.client;

import com.plain.cxf.UserDTO;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;

public class ClientByObject {
    public static void main(String[] args) {
        String format = MediaType.APPLICATION_XML;
        WebClient client = WebClient.create("http://localhost:8080/cxf/jaxrs");
        client.path("/user/addUser").accept(format).type(format);

        UserDTO userDTO = new UserDTO();
        userDTO.setName("userName");
        userDTO.setLoginName("loginName");
        UserDTO returnUser = client.post(userDTO, UserDTO.class);
        System.out.println("userName: " + returnUser.getName()
                + " lgoinName: " + returnUser.getLoginName());
    }
}
