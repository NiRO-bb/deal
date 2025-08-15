package com.example.Deal.Repository;

import com.example.Deal.DTO.RabbitMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RabbitMessageRepository extends JpaRepository<RabbitMessage, String> {
}
