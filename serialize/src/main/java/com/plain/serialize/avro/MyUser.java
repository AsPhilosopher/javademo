package com.plain.serialize.avro;

import org.apache.avro.Schema;

public class MyUser implements org.apache.avro.specific.SpecificRecord {
    public static final Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"MyUser\",\"namespace\":\"com.plain.serialize.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"age\",\"type\":[\"int\",\"null\"]},{\"name\":\"address\",\"type\":[\"string\",\"null\"]}]}");
    private CharSequence name;
    private Integer age;
    private CharSequence address;

    public Schema getSchema() {
        return SCHEMA$;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public CharSequence getAddress() {
        return address;
    }

    public void setAddress(CharSequence address) {
        this.address = address;
    }

    @Override
    // Used by DatumWriter.  Applications should not call.
    public java.lang.Object get(int field$) {
        switch (field$) {
            case 0:
                return name;
            case 1:
                return age;
            case 2:
                return address;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    // Used by DatumReader.  Applications should not call.
    @SuppressWarnings(value = "unchecked")
    public void put(int field$, java.lang.Object value$) {
        switch (field$) {
            case 0:
                name = (CharSequence) value$;
                break;
            case 1:
                age = (Integer) value$;
                break;
            case 2:
                address = (CharSequence) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "name=" + name +
                ", age=" + age +
                ", address=" + address +
                '}';
    }
}
