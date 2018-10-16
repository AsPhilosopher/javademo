package com.plain.java.jvm.classloader;

import java.io.*;

public class MyClassLoader extends ClassLoader {

    private String path;   //类的加载路径

    public MyClassLoader() {
    }

    public MyClassLoader(String path) {
        this.path = path;
    }


    //用于寻找类文件
    public Class findClass(String className, String prefix) {
        byte[] b = loadClassData(className);
        return defineClass(prefix + className, b, 0, b.length);
    }

    //用于加载类文件
    private byte[] loadClassData(String className) {

        className = path + className + ".class";
        //使用输入流读取类文件
        InputStream in = null;
        //使用byteArrayOutputStream保存类文件。然后转化为byte数组
        ByteArrayOutputStream out = null;
        try {
            in = new FileInputStream(new File(className));
            out = new ByteArrayOutputStream();
            int i = 0;
            while ((i = in.read()) != -1) {
                out.write(i);
            }

        } catch (Exception e) {
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return out.toByteArray();

    }
}
