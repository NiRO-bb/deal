package com.example.Deal.Repository;

import com.example.Deal.DTO.Type;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends CrudRepository<Type, String> {
}
