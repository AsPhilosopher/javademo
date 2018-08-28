package com.plain.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheRecovery {
    //Access time => Write/Update/Read
    @Test
    public void testEvictionByAccessTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterAccess(2, TimeUnit.SECONDS) //设置过期时间
                .build(CacheCreate.createCacheLoaderForString());
        cache.getUnchecked("wangji");
        TimeUnit.SECONDS.sleep(3);
        String val = cache.getIfPresent("wangji"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(2);
        val = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        cache.getUnchecked("cache");
        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("cache"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("cache"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));
    }

    //Write time => write/update
    @Test
    public void testEvictionByWriteTime() throws ExecutionException, InterruptedException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(CacheCreate.createCacheLoaderForString());
        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(2);
        String val = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        cache.put("guava", "cache"); //手动插入，能延长寿命
        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        cache.put("guava", "cache"); //手动插入，能延长寿命
        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("guava"); //读并不难延长寿命
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));

        TimeUnit.SECONDS.sleep(1);
        val = cache.getIfPresent("guava"); //读并不难延长寿命
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));
    }

    /**
     * 比较时注意用==而不是equals
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testWeakKey() throws ExecutionException, InterruptedException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .weakKeys()
                .weakValues()
//                .softValues()
                .build(CacheCreate.createCacheLoaderForString());
        cache.getUnchecked("guava");
        cache.getUnchecked("wangji");

        System.gc();
        TimeUnit.MILLISECONDS.sleep(1000);
        String val = cache.getIfPresent("guava"); //不会重新加载创建cache
        System.out.println(cache.getIfPresent("wangji"));
        System.out.println("被销毁：" + (val == null ? "是的" : "否"));
    }
}
