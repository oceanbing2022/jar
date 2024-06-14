package com.ohx.entity;

import lombok.Data;

@Data
public class Region {
    private Integer regionId;
    private String regionName;
    private Integer groupId;
    private Integer state;
    private String minLon;
    private String maxLon;
    private String minLat;
    private String maxLat;
    private String conditions;
}
