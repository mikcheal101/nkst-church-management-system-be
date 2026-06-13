package com.mikkytrionze.nkst.shared.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurationMap = new HashMap<>();

        cacheConfigurationMap.put(
                "churches",
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofHours(10)));

        cacheConfigurationMap.put(
                "pastors",
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofHours(1)));

        cacheConfigurationMap.put(
                "pastors_page",
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofHours(1)));

        cacheConfigurationMap.put(
                "churches_page",
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofHours(10)));

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(10)))
                .withInitialCacheConfigurations(cacheConfigurationMap)
                .build();
    }
}
