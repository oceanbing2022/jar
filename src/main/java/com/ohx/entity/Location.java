package com.ohx.entity;

import lombok.Data;

@Data
public class Location {
    private Integer id;
    private String name;
    private String locode;
    private String cnName;
    private Float lon;
    private Float lat;
    private boolean isBig;
}
