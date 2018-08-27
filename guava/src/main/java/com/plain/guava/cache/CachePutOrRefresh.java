package com.plain.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
}
