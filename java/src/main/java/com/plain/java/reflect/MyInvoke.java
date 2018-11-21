package com.plain.java.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvoke implements InvocationHandler {
    private Object object;

    public MyInvoke(Object object) {
        this.object = object;
    }

    /**
     *
     * @param proxy 代理类实例
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass().getName());
        System.out.println("Before");
        Object result = method.invoke(object, args);
        System.out.println("result: " + result);
        System.out.println("After");
//        return result;
        return proxy;
    }
}
