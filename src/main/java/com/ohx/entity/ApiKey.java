package com.ohx.entity;

import lombok.Data;

@Data
public class ApiKey {
    private Integer id;
    private String name;
    private Long count;
    private String updateTime;
    private Long endTime;
    private Boolean openRecord;
}
