package com.example.Deal.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    @JsonIgnore
    private String typeId;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Type type;

    @Column(name = "status_id")
    @JsonIgnore
    private String statusId = "DRAFT";

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Status status;

    @Column(name = "close_dt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeDt;

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

    @OneToMany(mappedBy = "dealId", cascade = CascadeType.ALL)
    private List<DealSum> sum;

    @OneToMany(mappedBy = "dealId", cascade = CascadeType.ALL)
    private List<DealContractor> contractors;

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
