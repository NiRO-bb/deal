package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contractor_to_role")
public class ContractorToRole implements Serializable {

    @EmbeddedId
    @JsonUnwrapped
    private Key key;

    @Schema(hidden = true)
    @ManyToOne
    @MapsId("contractorId")
    @JoinColumn(name = "contractor_id")
    @JsonBackReference("contractor_reference")
    private DealContractor contractor;

    @Schema(hidden = true)
    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @JsonBackReference("role_reference")
    private ContractorRole role;

    @Schema(hidden = true)
    @Column(name = "is_active")
    private boolean isActive = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Key implements Serializable {

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
