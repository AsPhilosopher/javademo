package com.plain.java.javaagent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class FirstAgent implements ClassFileTransformer {
    public final String injectedClassName = "com.plain.java.test.App";
    public final String methodName = "hello";

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        System.out.println("Class Name: " + className);
        if (className.equals(injectedClassName)) {
            CtClass ctclass;
            try {
                ctclass = ClassPool.getDefault().get(className);
                // 得到这方法实例
                CtMethod ctmethod = ctclass.getDeclaredMethod(methodName);
                ctmethod.insertBefore("System.out.println(11111111);");
                return ctclass.toBytecode();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
