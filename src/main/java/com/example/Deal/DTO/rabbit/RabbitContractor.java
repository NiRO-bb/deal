package com.example.Deal.DTO.rabbit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RabbitContractor {

    private String id;
    private String parentId;
    private String name;
    private String nameFull;
    private String inn;
    private String ogrn;
    private String country;
    private int industry;
    private int orgForm;
    private Date createDate;
    private Date modifyDate;
    private String createUserId;
    private String modifyUserId;
    private boolean isActive;

}
