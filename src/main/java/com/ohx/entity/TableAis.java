package com.ohx.entity;

import lombok.Data;

@Data
public class TableAis {
    private Long id;
    private Integer regionId;
    private Integer aisStatus;
    private String mmsi;
    private String time;
    private String lon;
    private String lat;
}
