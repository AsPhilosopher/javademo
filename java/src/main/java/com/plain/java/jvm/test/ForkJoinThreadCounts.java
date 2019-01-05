package com.plain.java.jvm.test;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * 结论：
 * 1.IO密集型比计算密集型更适合进行并发优化
 * 2.ForkJoin线程数与parallelism相关(<=parallelism)，但是如果任务不频繁，会回收线程，但貌似这个开销不大
 */
public class ForkJoinThreadCounts {
    private int cycle = 100;
    private int threadCount = Runtime.getRuntime().availableProcessors() << 1;
    private Runnable ioRun = () -> {
        for (long i = 0; i < 1000; i++) {
            fun();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };
    private Runnable calRun = () -> {
        for (long i = 0; i < 500000000; i++) {
            fun();
        }
    };
    private Runnable plainRun = () -> {
        for (int i = 0; i < 1000; i++) {
            fun();
        }
    };

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            ioRun.run();
        }
        long end = System.currentTimeMillis();
        System.out.println("ioRun" + (end - start));
    }

    @Test
    public void test0() {
        long start = System.currentTimeMillis();
        for (int j = 0; j < cycle; j++) {
            calRun.run();
        }
        long end = System.currentTimeMillis();
        System.out.println("calRun" + (end - start));
    }

    /**
     * ForkJoinPool-I/O密集型
     */
    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        for (int j = 0; j < cycle; j++) {
            forkJoinPool.execute(ioRun);
        }
        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        System.out.println("ForkJoinPool-I/O密集型" + (end - start));
    }

    /**
     * ForkJoinPool-计算密集型
     */
    @Test
    public void test2() {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        for (int j = 0; j < cycle; j++) {
            forkJoinPool.execute(calRun);
        }
        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        System.out.println("ForkJoinPool-计算密集型" + (end - start));
    }

    /**
     * ExecutorService-I/O密集型
     */
    @Test
    public void test3() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int j = 0; j < cycle; j++) {
            executorService.execute(ioRun);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        long end = System.currentTimeMillis();
        System.out.println("ExecutorService-I/O密集型" + (end - start));
    }

    /**
     * ExecutorService-计算密集型
     */
    @Test
    public void test4() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int j = 0; j < cycle; j++) {
            executorService.execute(calRun);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        long end = System.currentTimeMillis();
        System.out.println("ExecutorService-计算密集型" + (end - start));
    }

    @Test
    public void test5() throws InterruptedException {
        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        for (int j = 0; j < 16000; j++) {
            forkJoinPool.execute(plainRun);
            if (j % 16 == 0) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        System.out.println("ForkJoinPool-线程维护开销" + (end - start));
    }

    @Test
    public void test6() throws InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int j = 0; j < 16000; j++) {
            executorService.execute(plainRun);
            if (j % 16 == 0) {
                TimeUnit.MILLISECONDS.sleep(10);
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        long end = System.currentTimeMillis();
        System.out.println("ExecutorService-线程维护开销" + (end - start));

    }

    private void fun() {
        int a = 0;
        a += 10;
    }
}
