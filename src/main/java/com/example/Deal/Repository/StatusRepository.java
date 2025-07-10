package com.example.Deal.Repository;

import com.example.Deal.DTO.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, String> {
}
