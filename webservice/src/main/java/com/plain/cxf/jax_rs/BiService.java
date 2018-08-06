package com.plain.cxf.jax_rs;

public class BiService {
    public BiPerson findBiPersonByPersonCode(String personCode) {
        return new BiPerson(personCode);
    }
}
