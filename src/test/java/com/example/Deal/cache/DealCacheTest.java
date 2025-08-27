package com.example.Deal.cache;

import com.example.Deal.AbstractContainer;
import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.request.ChangeStatus;
import com.example.Deal.Service.DealService;
import com.example.Deal.Service.cached.DealCachedService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

import static com.example.Deal.util.TestUtil.getDeal;

@SpringBootTest
public class DealCacheTest extends AbstractContainer {

    @Autowired
    private DealService service;

    @Autowired
    private DealCachedService cachedService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testCacheAfterFirstCall() {
        UUID id = saveDeal().getId();
        Assertions.assertFalse(redisTemplate.hasKey(getKey(id)));
        cachedService.get(id);
        Assertions.assertTrue(redisTemplate.hasKey(getKey(id)));
    }

    @Test
    public void testRemoveCacheAfterStatusUpdating() {
        UUID id = saveDeal().getId();
        cachedService.get(id);
        Assertions.assertTrue(redisTemplate.hasKey(getKey(id)));
        service.change(new ChangeStatus(id, "DRAFT"));
        Assertions.assertFalse(redisTemplate.hasKey(getKey(id)));
    }

    @Test
    public void testRemoveCacheAfterDealUpdate() {
        Deal deal = saveDeal();
        UUID id = deal.getId();
        cachedService.get(id);
        Assertions.assertTrue(redisTemplate.hasKey(getKey(id)));
        service.save(deal);
        Assertions.assertFalse(redisTemplate.hasKey(getKey(id)));
    }

    private Deal saveDeal() {
        return service.save(getDeal());
    }

    private String getKey(UUID id) {
        return "deals::" + id;
    }

}
