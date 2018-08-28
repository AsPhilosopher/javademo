package com.plain.guava.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CacheCreate {
    /**
     * 创建 CacheLoader
     *
     * @throws ExecutionException
     */
    @Test
    public void test() throws ExecutionException {
        LoadingCache<String, Map> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(30L, TimeUnit.MILLISECONDS)
                .build(createCacheLoader());
        System.out.println(cache.get("init").get("init"));
        System.out.println(cache.get("init").get("init"));
    }

    public static CacheLoader<String, Map> createCacheLoader() {
        return new CacheLoader<String, Map>() {
            @Override
            public Map load(String key) throws Exception {
                System.out.println("加载创建key:" + key);
                Map map = new HashMap();
                map.put(key, "INIT");
                return map;
            }
        };
    }

    public static CacheLoader<String, String> createCacheLoaderForString() {
        return new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                System.out.println("加载创建key:" + key);
                return key;
            }
        };
    }

    /**
     * 创建 Callable
     *
     * @throws ExecutionException
     */
    @Test
    public void test1() throws ExecutionException {
        Cache<String, Map> cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build();
        Map _map = cache.get("init",
                /**
                 * If the key wasn't in the "easy to compute" group, we need to
                 * do things the hard way.
                 */
                () -> {
                    System.out.println("加载创建key:" + "init");
                    Map map = new HashMap();
                    map.put("init", "INIT");
                    return map;
                });
        System.out.println(_map.get("init"));
        System.out.println(cache.getIfPresent("init").get("init"));
    }
}
