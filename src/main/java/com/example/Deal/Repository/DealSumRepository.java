package com.example.Deal.Repository;

import com.example.Deal.DTO.DealSum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DealSumRepository extends CrudRepository<DealSum, Long> {

    List<DealSum> findByDealId(UUID dealId);

}
