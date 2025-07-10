package com.example.Deal.Repository;

import com.example.Deal.DTO.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DealRepository extends JpaRepository<Deal, UUID>, JpaSpecificationExecutor<Deal> {}
