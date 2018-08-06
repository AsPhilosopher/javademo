package com.plain.cxf.jax_rs;

import com.plain.cxf.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UserJaxRsServiceImpl implements UserJaxRsService {
    @Autowired
    private BiService biService;//业务系统类接口

    @Override
    public UserDTO getAsXml(String personCode) {
        BiPerson person = biService.findBiPersonByPersonCode(personCode);
        if (person == null) {
            String message = "用户不存在(id:" + personCode + ")";
            throw buildException(Response.Status.NOT_FOUND, message);
        }
        UserDTO dto = new UserDTO();
        dto.setName(person.getPersonName());
        dto.setLoginName(person.getPersonId());
        return dto;
    }

    @Override
    public UserDTO addUser(UserDTO user) {
        System.out.println(user);
        user.setName("jojo##12321321");
        return user;
    }

    private WebApplicationException buildException(Response.Status status, String message) {
        return new WebApplicationException(Response.status(status).entity(
                message).type(MediaType.TEXT_PLAIN).build());
    }

    public void setBiService(BiService biService) {
        this.biService = biService;
    }
}
