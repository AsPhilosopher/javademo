package com.plain.java.reflect;

public class PlainObject implements Plain {
    @Override
    public void fun() {
        System.out.println("Fun");
    }

    @Override
    public Plain work() {
        System.out.println("work");
        return this;
    }
}
