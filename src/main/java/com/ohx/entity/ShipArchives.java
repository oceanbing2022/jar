package com.ohx.entity;

import lombok.Data;

@Data

public class ShipArchives {
    private int id;
    private String imo;
    private String mmsi;
    private String callSign;
    private String nameEn;
    private String nameCn;
    private String flag;
    private String totalTon;
    private String loadTon;
    private String buildDate;
    private String typeCode;
    private String typeNameEn;
    private String typeNameCn;
    private String statusCode;
    private String statusString;
    private String length;
    private String wide;
    private String depth;
    private String freeboard;
    private String designSpeed;
    private String designDraft;
    private String mainEngineSpeed;
}
