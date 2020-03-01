package com.plain.guava.cache.classify;

import com.google.common.cache.*;
import com.google.common.util.concurrent.*;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CacheEvictionAndRefresh {
    RemovalListener<String, String> removalListener = new RemovalListener<String, String>() {
        public void onRemoval(RemovalNotification<String, String> removal) {
            System.out.println("remove cause: " + removal.getCause().name() +
                    String.format(" key:%s value:%s", removal.getKey(), removal.getValue()));
        }
    };

    RemovalListener<InnerObject, String> removalListener1 = new RemovalListener<InnerObject, String>() {
        public void onRemoval(RemovalNotification<InnerObject, String> removal) {
            System.out.println("remove cause: " + removal.getCause().name() +
                    String.format(" key:%s value:%s", removal.getKey(), removal.getValue()));
        }
    };

    RemovalListener<String, InnerObject> removalListener2 = new RemovalListener<String, InnerObject>() {
        public void onRemoval(RemovalNotification<String, InnerObject> removal) {
            System.out.println("remove cause: " + removal.getCause().name() +
                    String.format(" key:%s value:%s", removal.getKey(), removal.getValue()));
        }
    };

    private Cache<String, String> cache;

    @Before
    public void before() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build();
    }

    @Test
    public void timedEviction() throws InterruptedException {
        cache.put("KEY", "VALUE");
        TimeUnit.SECONDS.sleep(10);
        cache.put("KEY1", "VALUE1");
    }

    @Test
    public void sizeBasedEviction() {
        cache.put("KEY0", "VALUE0");
        cache.put("KEY1", "VALUE1");
        cache.put("KEY2", "VALUE2");
    }

    @Test
    public void explicitRemovals() throws InterruptedException {
        cache.put("KEY0", "VALUE0");
        cache.invalidate("KEY0");

        cache.put("KEY1", "VALUE1");
        cache.put("KEY2", "VALUE2");
        cache.invalidateAll(Arrays.asList("KEY1", "KEY2"));

        cache.put("KEY3", "VALUE3");
        cache.put("KEY4", "VALUE4");
        cache.invalidateAll();
    }

    @Test
    public void weakKeysEviction() throws InterruptedException {
        Cache<InnerObject, String> weakKeysCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener1)
                .weakKeys()
                .build();

        InnerObject key = new InnerObject("KEY");
        weakKeysCache.put(key, "VALUE");
        WeakReference<InnerObject> weakReference = new WeakReference(key);
        key = null;
        System.out.println(weakReference.get());
        System.gc();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(weakReference.get());
        System.out.println(weakKeysCache.getIfPresent(new InnerObject("KEY")));
        System.out.println(weakKeysCache.size());
    }

    @Test
    public void weakValuesEviction() throws InterruptedException {
        Cache<String, InnerObject> weakValuesCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener2)
                .weakValues()
                .build();

        InnerObject value = new InnerObject("VALUE");
        String key = "key";
        weakValuesCache.put(key, value);
        WeakReference<InnerObject> weakReference = new WeakReference(value);
        value = null;
        System.out.println(weakReference.get());
        System.gc();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(weakReference.get());
        System.out.println(weakValuesCache.getIfPresent(key));
        System.out.println(weakValuesCache.size());
    }

    @Test
    public void softValuesEviction() throws InterruptedException {
        Cache<String, InnerObject> softValuesCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener2)
                .softValues()
                .build();

        InnerObject value = new InnerObject("VALUE");
        String key = "key";
        softValuesCache.put(key, value);
        SoftReference<InnerObject> softReference = new SoftReference(value);
        value = null;
        System.out.println(softReference.get());
        System.gc();
        TimeUnit.SECONDS.sleep(10);
        System.out.println(softReference.get());
        System.out.println(softValuesCache.getIfPresent(key));
        System.out.println(softValuesCache.size());
    }

    @Test
    public void refresh() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
        final String VALUE = "VALUE";

        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return "form: " + key;
            }

            @Override
            public ListenableFuture<String> reload(String oldKey, String oldValue) throws Exception {
                if (VALUE.equals(oldValue)) {
                    return Futures.immediateFuture(oldValue);
                }
                ListenableFutureTask<String> listenableFutureTask = ListenableFutureTask.create(() -> {
                    return String.format("key:%s old:%s", oldKey, oldValue);
                });
                listeningExecutorService.execute(listenableFutureTask);
                return listenableFutureTask;
            }
        };

        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(2)
                .removalListener(removalListener)
                .refreshAfterWrite(3, TimeUnit.SECONDS)
                .build(cacheLoader);

        String key = "key";
        loadingCache.refresh(key);
        System.out.println(loadingCache.getUnchecked(key));
        loadingCache.refresh(key);
        System.out.println(loadingCache.getUnchecked(key));

        String KEY = "KEY";
        loadingCache.put(KEY, "value");
        TimeUnit.SECONDS.sleep(5);
        System.out.println(loadingCache.getUnchecked(KEY));
        TimeUnit.SECONDS.sleep(2);
        System.out.println(loadingCache.getUnchecked(KEY));

        loadingCache.put(KEY, VALUE);
        TimeUnit.SECONDS.sleep(5);
        System.out.println(loadingCache.getUnchecked(KEY));
    }

    @Data
    class InnerObject {
        private String key;

        public InnerObject(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerObject innerKey = (InnerObject) o;
            return Objects.equals(key, innerKey.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
