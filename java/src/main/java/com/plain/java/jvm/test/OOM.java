package com.plain.java.jvm.test;

import com.plain.java.jvm.test.bean.BigObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * -Xms64m
 * -Xmx64m
 * -XX:+PrintGCDetails
 *
 * jmap -dump:live,file=dump.hprof [pid]
 */
public class OOM {
    public static void main(String[] args) throws InterruptedException {
        BigObject bigObject = new BigObject();
        bigObject.setList(new ArrayList<>());
        int i = 0;
        while (true) {
            bigObject.getList().add(i++);
            if (i >= 2734000) {
                System.out.println(i);
                TimeUnit.MILLISECONDS.sleep(500);
            }
        }
    }
}
