package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal_contractor")
public class DealContractor implements Serializable {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(name = "deal_id")
    @JsonProperty("deal_id")
    private UUID dealId;

    @Column(name = "contractor_id")
    @JsonProperty("contractor_id")
    private String contractorId;

    private String name;

    private String inn;

    private boolean main = false;

    @Column(name = "create_date")
    @JsonIgnore
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "modify_date")
    @JsonIgnore
    private LocalDateTime modifyDate;

    @Column(name = "create_user_id")
    @JsonIgnore
    private String createUserId;

    @Column(name = "modify_user_id")
    @JsonIgnore
    private String modifyUserId;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive = true;

    @OneToMany(mappedBy = "contractor", fetch = FetchType.LAZY)
    @JsonManagedReference("contractor_reference")
    private List<ContractorToRole> roles;

    @JsonIgnore
    public List<ContractorRole> getRolesOnly() {
        return roles.stream()
                .map(ContractorToRole::getRole)
                .collect(Collectors.toList());
    }

}
