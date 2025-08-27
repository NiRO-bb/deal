package com.example.Deal.util;

import com.example.Deal.DTO.Deal;
import com.example.Deal.DTO.DealContractor;
import com.example.Deal.DTO.rabbit.RabbitContractor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public final class TestUtil {

    private TestUtil() {}

    public static Deal getDeal() {
        return new Deal(
                UUID.randomUUID(),
                "test",
                "test",
                LocalDate.now(),
                LocalDateTime.now(),
                LocalDate.now(),
                "OTHER",
                "CLOSED",
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "test",
                "test",
                true
        );
    }

    public static DealContractor getDealContractor(UUID dealId) {
        return new DealContractor(
                UUID.randomUUID(),
                dealId,
                "test",
                "test",
                "test",
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "test",
                "test",
                true,
                null
        );
    }

    public static RabbitContractor getRabbitContractor() {
        return new RabbitContractor(
                "test",
                null,
                "new test",
                "test",
                "test",
                "test",
                "test",
                0,
                0,
                new Date(),
                new Date(),
                "test",
                "test",
                true
        );
    }

}
