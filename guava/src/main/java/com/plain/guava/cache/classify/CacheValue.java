package com.plain.guava.cache.classify;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class CacheValue {
    @Test
    public void fromCacheLoad() {
        // 这里不写成Lambda，方便阅读
        CacheLoader cacheLoader = new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return "form: " + key;
            }
        };

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .build(cacheLoader);

        System.out.println(loadingCache.getUnchecked("KEY"));
    }

    @Test
    public void fromCallable() throws ExecutionException {
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        String key = "KEY";
        String value = cache.get(key, new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "form: " + key;
            }
        });
        System.out.println(value);
    }

    @Test
    public void insertDirect() {
        Cache<String, String> cache = CacheBuilder.newBuilder().build();
        cache.put("key", "value");
        System.out.println(cache.getIfPresent("key"));
        System.out.println(cache.getIfPresent("#key#"));
    }
}
