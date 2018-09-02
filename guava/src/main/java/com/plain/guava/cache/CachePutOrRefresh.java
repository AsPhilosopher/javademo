package com.plain.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CachePutOrRefresh {
    private LoadingCache getCacheLoader() {
        LoadingCache<String, Map> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(30L, TimeUnit.MILLISECONDS)
                .build(CacheCreate.createCacheLoader());
        return cache;
    }

    /**
     * 显示插入
     */
    @Test
    public void test() throws ExecutionException {
        LoadingCache<String, Map> cache = getCacheLoader();
        Map map = new HashMap();
        map.put("aa", "bb");
        //显示插入
        cache.put("aa", map);
        System.out.println(cache.get("aa").get("aa"));
        /**
         * 使用Cache.asMap()视图提供的任何方法也能修改缓存。
         * 但请注意，asMap视图的任何方法都不能保证缓存项被原子地加载到缓存中。
         * 进一步说，asMap视图的原子运算在Guava Cache的原子加载范畴之外
         */
        cache.asMap().putIfAbsent("bb", map);
        System.out.println(cache.get("bb").get("aa"));
    }

    @Test
    public void testSize() {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build(CacheCreate.createCacheLoaderForString());
        cache.getUnchecked("wangji");
        cache.getUnchecked("wangwang");
        cache.getUnchecked("old wang");
        System.out.println(cache.size());

        cache.getUnchecked("new wang");
        String val = cache.getIfPresent("wangji"); //getIfPresent不会重新加载创建cache
        System.out.println("最新的把老的替换掉：" + (val == null ? "是的" : "否"));
        val = cache.getIfPresent("new wang");
        System.out.println("获取结果：" + val);
    }

    /**
     * 要注意回收也是在重量逼近限定值时就进行了，还要知道重量是在缓存创建时计算的，因此要考虑重量计算的复杂度
     * 最大权重是每段平分的，某段上元素总重量超过该段的最大权重，就会被清理
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testWeight() throws ExecutionException {
        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
                .maximumWeight(100)
                .weigher((Weigher<String, String>) (key, val) -> {
                    int weight = val.length() * 10;//权重计算器
                    System.out.println("weight is :" + weight);
                    return weight;
                })
                .build(CacheCreate.createCacheLoaderForString());
        System.out.println(cache.get("w"));
        System.out.println("cacheSize：" + cache.size());
        System.out.println(cache.get("wangwangyuyu"));
        System.out.println("cacheSize：" + cache.size());
        System.out.println(cache.get("old wang"));
        System.out.println("cacheSize：" + cache.size());
        System.out.println(cache.get("n"));
        System.out.println("cacheSize：" + cache.size());
    }

    @Test
    public void testCacheRefresh() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        CacheLoader<String, Long> cacheLoader = CacheLoader
                .from(k -> {
                    counter.incrementAndGet();
                    System.out.println(counter.get() + " 创建 key :" + k);
                    return System.currentTimeMillis();
//                    return 100L;
                });
        //注意：缓存项只有在被检索时才会真正刷新（如果CacheLoader.refresh实现为异步，那么检索不会被刷新拖慢）
        LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS) // 2s后重新刷新
                .build(cacheLoader);

        Long result1 = cache.getUnchecked("guava");
        TimeUnit.SECONDS.sleep(3);
        Long result2 = cache.getUnchecked("guava");
        System.out.println("是否刷新: " + (result1.longValue() != result2.longValue() ? "是的" : "否"));
    }
}
