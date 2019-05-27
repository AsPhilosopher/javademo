package com.plain.resilience4j;

import com.plain.resilience4j.service.BackendService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.internal.RetryImpl;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.vavr.API;
import io.vavr.CheckedFunction0;
import io.vavr.Predicates;
import io.vavr.control.Try;
import org.junit.Before;
import org.junit.Test;

import javax.xml.ws.WebServiceException;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TestDemo {
    private BackendService backendService;

    @Before
    public void before() {
        this.backendService = new BackendService();
    }

    /**
     * failureRateThreshold，默认为50，即失败率阈值为50%
     * ringBufferSizeInHalfOpenState，设置当断路器处于HALF_OPEN状态下的ring buffer的大小，它存储了最近一段时间请求的成功失败状态，默认为10
     * ringBufferSizeInClosedState，设置当断路器处于CLOSED状态下的ring buffer的大小，它存储了最近一段时间请求的成功失败状态，默认为100
     * waitDurationInOpenState，用来指定断路器从OPEN到HALF_OPEN状态等待的时长，默认是60秒
     * recordFailurePredicate，用于判断哪些异常应该算作失败纳入断路器统计，默认是Throwable类型
     * automaticTransitionFromOpenToHalfOpenEnabled，当waitDurationInOpenState时间一过，是否自动从OPEN切换到HALF_OPEN，默认为true
     *
     * @throws InterruptedException
     */
    @Test
    public void testCircuitBreaker() throws InterruptedException {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(30)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .ringBufferSizeInHalfOpenState(5)
                .ringBufferSizeInClosedState(10)
                .build();

        final Random random = new Random(100);
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("uniqueName", circuitBreakerConfig);

        CheckedFunction0<String> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, () -> {
                    long num = random.nextInt(100);
                    if (num > 0 && num < 30) {
                        throw new RuntimeException("EX:" + num);
                    }
                    return "" + num;
                });
        int index = 0;
        for (; index < 100; index++) {
            Try<String> result = Try.of(decoratedSupplier);
            result.onFailure(f -> System.out.println(f.getMessage() + " > " + circuitBreaker.getState()));
            result.onSuccess(f -> System.out.println(f + " = " + circuitBreaker.getState()));
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }

    @Test
    public void testTimeLimiter() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendName");
        RetryConfig retryConfig = RetryConfig.custom().maxAttempts(1).waitDuration(Duration.ofMillis(100)).build();
        Retry retry = new RetryImpl("backendName", retryConfig);

        Supplier<String> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, backendService::doSomething);
        decoratedSupplier = Retry
                .decorateSupplier(retry, decoratedSupplier);

        Duration timeoutDuration = Duration.ofSeconds(1);
        TimeLimiter timeLimiter = TimeLimiter.of(timeoutDuration);
        Supplier<Future<String>> futureSupplier = () -> CompletableFuture.supplyAsync(() -> backendService.doSomething());

        Callable<String> callable = TimeLimiter.decorateFutureSupplier(timeLimiter, futureSupplier);
        try {
            System.out.println(callable.call());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DO FINALLY");
        }
    }

    @Test
    public void testRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(2) //最少重试次数
                .waitDuration(Duration.ofMillis(100)) //间隔多少时间发一次重试机制
                .retryOnException(throwable -> API.Match(throwable).of(
                        API.Case(API.$(Predicates.instanceOf(WebServiceException.class)), true),//表示调用方法如果Throw WebServiceException，则启动重试机制
                        API.Case(API.$(), false))) //其它异常对象忽略
                .build();
        Retry retry = Retry.of("dd", config);//装载可能会要重试方法
        CheckedFunction0<String> retryableSupplier = Retry.decorateCheckedSupplier(retry, () -> {
            System.out.println("1");
            throw new WebServiceException("Exception Message");
        });

        //调用方法，如果失败，会执行recover方法，进入异常修复方法。
        Try<String> result = Try.of(retryableSupplier).recover((throwable -> {
            System.out.println(throwable.getMessage());
            return "hello";
        }));
        System.out.println(result.get());
    }

    @Test
    public void demo() {
        // Create a CircuitBreaker (use default configuration)
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendName");

        // Create a Retry with at most 3 retries and a fixed time interval between retries of 500ms
        Retry retry = Retry.ofDefaults("backendName");

        // Decorate your call to BackendService.doSomething() with a CircuitBreaker
        Supplier<String> decoratedSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, backendService::doSomething);

        // Decorate your call with automatic retry
        decoratedSupplier = Retry
                .decorateSupplier(retry, decoratedSupplier);

        // Execute the decorated supplier and recover from any exception
        String result = Try.ofSupplier(decoratedSupplier)
                .recover(throwable -> "Hello from Recovery").get();
        System.out.println("retry: " + result);

        // When you don't want to decorate your lambda expression,
        // but just execute it and protect the call by a CircuitBreaker.
        result = circuitBreaker.executeSupplier(backendService::doSomething);
        System.out.println("circuitBreaker: " + result);
    }
}
