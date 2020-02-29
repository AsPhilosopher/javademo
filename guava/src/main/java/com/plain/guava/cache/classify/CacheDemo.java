package com.plain.guava.cache.classify;

import com.google.common.cache.*;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class CacheDemo {
    @Test
    public void test() throws InterruptedException {
        // 这里不写成Lambda，方便阅读
        RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
            public void onRemoval(RemovalNotification<String, String> removal) {
                System.out.println("remove cause: " + removal.getCause().name() +
                        String.format(" key:%s value:%s", removal.getKey(), removal.getValue()));
            }
        };

        CacheLoader cacheLoader = new CacheLoader() {
            @Override
            public Object load(Object key) throws Exception {
                return "form: " + key;
            }
        };

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build(cacheLoader);

        String key = "KEY";
        // 验证CacheLoader
        System.out.println(loadingCache.getUnchecked(key));
        // 验证maximumSize
        loadingCache.put("mykey0", "myvalue0");
        loadingCache.put("mykey1", "myvalue1");
        loadingCache.put("mykey2", "myvalue2");

        // put之后，get时就不会调用load方法
        loadingCache.put(key, "VALUE");
        System.out.println(loadingCache.getUnchecked(key));

        // 延迟时间到了，不会驱逐数据。而是在put或get数据时清除
        TimeUnit.SECONDS.sleep(10);
        System.out.println(loadingCache.getUnchecked(key));
        loadingCache.put("mykey3", "myvalue3");
    }
}
