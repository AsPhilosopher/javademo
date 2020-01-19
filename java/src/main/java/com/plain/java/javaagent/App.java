package com.plain.java.javaagent;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {
    public static void premain(String agentOps, Instrumentation inst) {
        System.out.println("=========premain方法开始========");
        System.out.println(agentOps);
        // 添加Transformer
        inst.addTransformer(new FirstAgent());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> System.out.println("@@@@"), 1, 3, TimeUnit.SECONDS);
        System.out.println("=========premain方法结束========");
    }
}
