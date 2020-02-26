package com.plain.java.thread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitSignal {
    @Test
    public void test() {
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();

        Thread thread1 = new Thread(() -> {
            while (true) {
                lock.lock();
                condition1.signalAll();
                System.out.println("@@@@@@");
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            while (true) {
                lock.lock();
                condition1.signalAll();
                System.out.println("$$$$$$");
                try {
                    condition1.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
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
