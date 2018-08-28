package com.plain.guava.cache;

import com.google.common.cache.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheCleanUp {
    @Test
    public void test() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS) //设置过期时间
                .build(CacheCreate.createCacheLoaderForString());
        cache.getUnchecked("aa");
        cache.getUnchecked("bb");
        cache.getUnchecked("cc");

        cache.invalidate("aa");
        System.out.println(cache.size());

        List<String> list = new ArrayList<>();
        list.add("bb");
        list.add("cc");
        cache.invalidateAll(list);
        System.out.println(cache.size());

        cache.getUnchecked("aa");
        cache.getUnchecked("bb");
        cache.getUnchecked("cc");
        cache.invalidateAll();
        System.out.println(cache.size());
    }

    /**
     * 移除监听器
     * <p>
     * 因为缓存的维护和请求响应通常是同时进行的，代价高昂的监听器方法在同步模式下会拖慢正常的缓存请求。
     * 在这种情况下，你可以使用RemovalListeners.asynchronous(RemovalListener, Executor)把监听器装饰为异步操作。
     */
    @Test
    public void testCacheRemovedNotification() {
        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
        RemovalListener<String, String> listener = notification ->
        {
            if (notification.wasEvicted()) {
                RemovalCause cause = notification.getCause();
                System.out.println("remove cacase is :" + cause.toString());
                System.out.println("key:" + notification.getKey() + "value:" + notification.getValue());
            }
        };
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .removalListener(listener)// 添加删除监听
                .build(loader);
        cache.getUnchecked("wangji");
        cache.getUnchecked("wangwang");
        cache.getUnchecked("guava");
        cache.getUnchecked("test");
        cache.getUnchecked("test1");
    }
}
