package com.plain.guava.cache.classify;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

public class CacheFeature {
    private Cache<String, String> cache;

    RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
        public void onRemoval(RemovalNotification<String, String> removal) {
            System.out.println("remove cause: " + removal.getCause().name() +
                    String.format(" key:%s value:%s", removal.getKey(), removal.getValue()));
        }
    };

    @Before
    public void before() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener)
                .recordStats()
                .build();
    }

    @Test
    public void statistics() {
        cache.put("key0", "value0");
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        cache.getIfPresent("key0");
        cache.getIfPresent("key3");
        System.out.println("averageLoadPenalty: " + cache.stats().averageLoadPenalty());
        System.out.println("hitRate: " + cache.stats().hitRate());
        System.out.println("evictionCount: " + cache.stats().evictionCount());
    }

    @Test
    public void asMap() {
        cache.put("key0", "value0");
        cache.put("key1", "value1");
        ConcurrentMap map = cache.asMap();
        System.out.println(map.get("key0"));
        map.put("key2", "value2");
        map.put("key3", "value3");
        System.out.println(cache.getIfPresent("key2"));
        cache.put("key4", "value4");
        System.out.println(map.get("key4"));
    }
}
