package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contractor_role")
@JsonPropertyOrder({"id", "name", "category"})
public class ContractorRole implements Serializable {

    @Id
    private String id;

    private String name;

    private String category;

    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private List<ContractorToRole> contractors;

}
