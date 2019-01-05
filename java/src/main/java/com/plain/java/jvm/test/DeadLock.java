package com.plain.java.jvm.test;

public class DeadLock {
    public static void main(String[] args) {
        Plain plain = new Plain();
        Thread thread0 = new Thread(() -> {
            while (true) {
                plain.funa();
            }
        });
        thread0.setName("MyThread-0");
        thread0.start();

        Thread thread1 = new Thread(() -> {
            while (true) {
                plain.funb();
            }
        });
        thread1.setName("MyThread-1");
        thread1.start();
    }

    static class Plain {
        public void funa() {
            synchronized (DeadLock.class) {
                System.out.println("AAAA");
                synchronized (Plain.class) {
                    System.out.println("ASAS");
                }
            }
        }

        public void funb() {
            synchronized (Plain.class) {
                System.out.println("BBBB");
                synchronized (DeadLock.class) {
                    System.out.println("BSBS");
                }
            }
        }
    }
}
