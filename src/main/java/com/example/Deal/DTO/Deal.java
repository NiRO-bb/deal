package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deal")
public class Deal {

    @Id
    private UUID id = UUID.randomUUID();

    private String description;

    @Column(name = "agreement_number")
    private String agreementNumber;

    @Column(name = "agreement_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate agreementDate;

    @Column(name = "agreement_start_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime agreementStartDt;

    @Column(name = "availability_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate availabilityDate;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "status_id")
    private String statusId = "DRAFT";

    @Column(name = "close_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeDt;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "modify_date")
    private LocalDateTime modifyDate;

    @Column(name = "create_user_id")
    private String createUserId;

    @Column(name = "modify_user_id")
    private String modifyUserId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealStatusUpdate {

        private UUID id;
        private String status;

        public String desc() {
            return String.format("{ \"id\":\"%s\", \"status\":\"%s\" }", id, status);
        }

    }

}
