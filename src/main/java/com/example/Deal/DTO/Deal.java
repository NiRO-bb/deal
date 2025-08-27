package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal")
public class Deal implements Serializable {

    @Id
    private UUID id = UUID.randomUUID();

    private String description;

    @Schema(name = "agreement_number")
    @Column(name = "agreement_number")
    private String agreementNumber;

    @Schema(name = "agreement_date", example = "2025-01-12")
    @Column(name = "agreement_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate agreementDate;

    @Schema(name = "agreement_start_dt", example = "2025-01-10 12:23:05")
    @Column(name = "agreement_start_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime agreementStartDt;

    @Schema(name = "availability_date", example = "2025-03-17")
    @Column(name = "availability_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate availabilityDate;

    @Schema(name = "type_id")
    @Column(name = "type_id")
    private String typeId;

    @Schema(name = "status_id")
    @Column(name = "status_id")
    private String statusId = "DRAFT";

    @Schema(name = "close_dt", example = "2025-05-11 05:13:01")
    @Column(name = "close_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeDt;

    @Schema(hidden = true)
    @Column(name = "create_date")
    @JsonIgnore
    private LocalDateTime createDate = LocalDateTime.now();

    @Schema(hidden = true)
    @Column(name = "modify_date")
    @JsonIgnore
    private LocalDateTime modifyDate;

    @Schema(hidden = true)
    @Column(name = "create_user_id")
    @JsonIgnore
    private String createUserId;

    @Schema(hidden = true)
    @Column(name = "modify_user_id")
    @JsonIgnore
    private String modifyUserId;

    @Schema(hidden = true)
    @Column(name = "is_active")
    @JsonIgnore
    private boolean isActive = true;

}
