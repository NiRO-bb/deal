package com.example.Deal.Repository;

import com.example.Deal.DTO.DealSum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealSumRepository extends CrudRepository<DealSum, Long> {

    List<DealSum> findByDealId(UUID dealId);

}
