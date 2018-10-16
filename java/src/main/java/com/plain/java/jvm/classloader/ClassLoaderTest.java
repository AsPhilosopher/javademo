package com.plain.java.jvm.classloader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {
//        String path = ClassLoaderTest.class.getResource("/").getPath() + "com/jvm/classload/";
        //找到class文件路径
        String path = System.getProperty("user.dir") + "/";
        MyClassLoader myClassLoader = new MyClassLoader(path);
        //类要全名，与class中的全名一致
        Class c = myClassLoader.findClass("Hello", "com.lincon.test.");
        Method method = c.getMethod("getWorld");
        System.out.println(method.invoke(c.newInstance()));

        System.out.println(c.getClassLoader());
        System.out.println(c.getName());
        System.out.println(ClassLoaderTest.class.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());

        Thread.currentThread().setContextClassLoader(c.getClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader());

        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println(Thread.currentThread().getContextClassLoader().loadClass("com.lincon.test.Hello"));
        //报错，无法在系统类加载器中找到这个类
//        System.out.println(ClassLoader.getSystemClassLoader().loadClass("com.lincon.test.Hello"));
    }
}
