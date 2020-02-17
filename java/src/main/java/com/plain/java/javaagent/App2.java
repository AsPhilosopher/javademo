package com.plain.java.javaagent;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App2 {
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法开始========");
        System.out.println("22222222222");
        System.out.println("=========premain方法结束========");
    }
}
