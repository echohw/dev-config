package com.example.devconfig.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Created by AMe on 2020-06-22 04:09.
 */
@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    @Primary
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager(RedisConnectionFactory factory, @Qualifier("objectToJsonRedisSerializer") RedisSerializer objectToJsonRedisSerializer) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(objectToJsonRedisSerializer))
            .entryTtl(Duration.ofHours(1));
        return new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
    }

}
