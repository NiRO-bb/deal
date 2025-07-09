package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contractor_to_role")
public class ContractorToRole {

    @EmbeddedId
    @JsonUnwrapped
    private Key key;
    @Column(name = "is_active")
    private boolean isActive = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Key {

        @Column(name = "contractor_id")
        @JsonProperty("contractor_id")
        private UUID contractorId;

        @Column(name = "role_id")
        @JsonProperty("role_id")
        private String roleId;

        public String desc() {
            return String.format("{ \"contractor_id\":\"%s\", \"role_id\":\"%s\" }", contractorId, roleId);
        }

    }

}
