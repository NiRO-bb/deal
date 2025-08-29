package com.example.Deal.cache;

import com.example.Deal.AbstractContainer;
import com.example.Deal.DTO.Type;
import com.example.Deal.Service.TypeService;
import com.example.Deal.Service.cached.TypeCachedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class TypeCacheTest extends AbstractContainer {

    private final String key = "deal_types::all";

    @Autowired
    private TypeService service;

    @Autowired
    private TypeCachedService cachedService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testCacheAfterFirstCall() {
        Assertions.assertFalse(redisTemplate.hasKey(key));
        cachedService.get();
        Assertions.assertTrue(redisTemplate.hasKey(key));
    }

    @Test
    public void testRemoveCacheAfterUpdating() {
        cachedService.get();
        Assertions.assertTrue(redisTemplate.hasKey(key));
        service.save(new Type("test", "test", true));
        Assertions.assertFalse(redisTemplate.hasKey(key));
    }

}
