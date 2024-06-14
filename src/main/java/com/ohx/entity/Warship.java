package com.ohx.entity;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@NodeEntity(label = "Warship")
public class Warship implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @Property
    private String CNname;
    @Property
    private String an_name;
    @Property
    private String call_sign;
    @Property
    private String country;
    @Property
    private String hull_number;
    @Property
    private String imo;
    @Property
    private String mmsi;
    @Property
    private String name;
    @Labels
    private ArrayList<String> labels;
}
