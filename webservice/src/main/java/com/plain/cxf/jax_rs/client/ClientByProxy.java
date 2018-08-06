package com.plain.cxf.jax_rs.client;

import com.plain.cxf.jax_rs.UserJaxRsService;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

public class ClientByProxy {
    public static void main(String[] args) {
        UserJaxRsService store = JAXRSClientFactory
                .create("http://localhost:8080/cxf/jaxrs",
                        UserJaxRsService.class);
        System.out.println(store.getAsXml("15811006"));
    }
}
