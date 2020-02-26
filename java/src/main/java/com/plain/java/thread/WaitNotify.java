package com.plain.java.thread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class WaitNotify {
    @Test
    public void test() {
        Object object = new Object();
        Thread thread1 = new Thread(() -> {
            while (true) {
                synchronized (object) {
                    object.notifyAll();
                    System.out.println("@@@@@@");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                synchronized (object) {
                    object.notifyAll();
                    System.out.println("$$$$$$");
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            TimeUnit.MINUTES.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
