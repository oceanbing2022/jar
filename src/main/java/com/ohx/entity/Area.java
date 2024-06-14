package com.ohx.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NodeEntity(label = "Area")
@NoArgsConstructor
@AllArgsConstructor
public class Area implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String name;
    @Property
    private String CNname;
    @Property
    private Float lon;//经度
    @Property
    private Float lat;//纬度
    @Property
    private String country;
    @Labels
    private ArrayList<String> labels;
    @Property
    private String general_name;
}
