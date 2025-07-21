package com.example.Deal.Controller;

import com.example.Deal.DTO.ContractorToRole;
import org.springframework.http.ResponseEntity;

public interface ContractorToRoleController {

    /**
     * Adds role to DealContractor entity.
     *
     * @param key contains contractor id and role id values
     * @return added role
     */
    ResponseEntity<?> add(ContractorToRole.Key key);

    /**
     * Logically deletes role of DealContractor entity.
     *
     * @param key contains contractor id and role id values
     * @return NO_CONTENT response code if successful, BAD_REQUEST else
     */
    ResponseEntity<?> delete(ContractorToRole.Key key);

}
