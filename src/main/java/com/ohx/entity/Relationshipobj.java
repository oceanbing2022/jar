package com.ohx.entity;

import lombok.Data;

@Data
public class Relationshipobj {//Warship Area
    private String For;//关系目的
    private Object from;//开始节点对象
    private Object to;//结束节点对象
    private Long time_start;//开始时间
    private Long time_end;//结束时间
    private String type;//节点类型
}
