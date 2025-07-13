package com.example.Deal.Repository;

import com.example.Deal.DTO.DealSum;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.stereotype.Repository;

@Repository
public interface DealSumRepository extends JpaAttributeConverter<DealSum, Long> {
}
