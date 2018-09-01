package com.java.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheTest {

    public static final  Cache<String, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();

    public static void main(String[] args) throws InterruptedException {
       cache.put("name", "hy");
        System.out.println(cache.getIfPresent("name"));
        System.out.println(cache.getIfPresent("age"));
        Thread.sleep(11000);
        System.out.println(cache.getIfPresent("name"));
    }
}
