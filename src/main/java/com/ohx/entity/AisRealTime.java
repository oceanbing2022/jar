package com.ohx.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AisRealTime {
    private Long timeStamp;
    private String mmsi;
    private Integer navStatus;
    private Integer rot;
    private Float sog;
    private Integer posAcc;
    private BigDecimal lon;
    private BigDecimal lat;
    private Float cog;
    private Float trueHead;
    private Integer dataId;
    private Long updateTime;
}
