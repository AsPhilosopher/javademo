package com.plain.cxf.jax_rs;

import com.plain.cxf.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/user")
public interface UserJaxRsService {
    @GET
    @Path("/{personCode}.xml")
    @Produces(MediaType.APPLICATION_XML)
    UserDTO getAsXml(@PathParam("personCode") String personCode);

    @POST
    @Path("/addUser")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    UserDTO addUser(UserDTO user);
}
