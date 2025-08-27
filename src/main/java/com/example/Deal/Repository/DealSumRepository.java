package com.example.Deal.Repository;

import com.example.Deal.DTO.DealSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealSumRepository extends JpaRepository<DealSum, Long> {
}
