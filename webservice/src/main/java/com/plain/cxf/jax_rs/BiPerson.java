package com.plain.cxf.jax_rs;

public class BiPerson {
    private String personName;
    private String personId;

    public BiPerson(String personId) {
        this.personId = personId;
        this.personName = "Lincon";
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
