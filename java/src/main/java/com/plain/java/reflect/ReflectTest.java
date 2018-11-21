package com.plain.java.reflect;

import org.junit.Test;

import java.lang.reflect.Proxy;

public class ReflectTest {
    @Test
    public void test() {
        Plain plain = new PlainObject();
        MyInvoke myInvoke = new MyInvoke(plain);
        Plain proxy = (Plain) Proxy.newProxyInstance(plain.getClass().getClassLoader(), plain.getClass().getInterfaces(), myInvoke);
//        proxy.fun();
        Plain p = proxy.work();
        p.fun();
    }
}
