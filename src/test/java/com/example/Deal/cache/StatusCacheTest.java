package com.example.Deal.cache;

import com.example.Deal.AbstractContainer;
import com.example.Deal.Service.cached.StatusCachedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class StatusCacheTest extends AbstractContainer {

    private final String key = "deal_statuses::all";

    @Autowired
    private StatusCachedService cachedService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testCacheAfterFirstCall() {
        Assertions.assertFalse(redisTemplate.hasKey(key));
        cachedService.get();
        Assertions.assertTrue(redisTemplate.hasKey(key));
    }

}
