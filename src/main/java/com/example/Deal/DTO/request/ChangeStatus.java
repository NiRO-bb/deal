package com.example.Deal.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStatus {

    private UUID id;

    private String status;

    public String desc() {
        return String.format("{ \"id\":\"%s\", \"status\":\"%s\" }", id, status);
    }

}
