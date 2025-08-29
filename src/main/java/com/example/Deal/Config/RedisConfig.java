package com.example.Deal.Config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Sets some configuration for Redis.
 * Defines different ttl of cached values.
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mapper.setDefaultTyping(
                new ObjectMapper.DefaultTypeResolverBuilder(
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        mapper.getPolymorphicTypeValidator())
                        .init(JsonTypeInfo.Id.CLASS, null)
                        .inclusion(JsonTypeInfo.As.PROPERTY));

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(mapper)));
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(cacheConfiguration())
                .withInitialCacheConfigurations(cacheConfig())
                .build();
    }

    /**
     * Sets TTL for cache with "deals" key.
     *
     * @return config as Map
     */
    private Map<String, RedisCacheConfiguration> cacheConfig() {
        Map<String, RedisCacheConfiguration> cacheConfig = new HashMap<>();
        cacheConfig.put("deals",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)));
        return cacheConfig;
    }

}
