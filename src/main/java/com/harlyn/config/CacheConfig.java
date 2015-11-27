package com.harlyn.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by wannabe on 27.11.15.
 */
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean(name = "cacheManager")
    public SimpleCacheManager cacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(new HashSet<>(Arrays.asList(
                new ConcurrentMapCache("default"),
                new ConcurrentMapCache("me")
        )));
        return simpleCacheManager;
    }
}
