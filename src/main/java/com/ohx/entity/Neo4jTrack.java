package com.ohx.entity;

import lombok.Data;

/**
 * @author 贾榕福
 * @date 2022/12/23
 */
@Data
public class Neo4jTrack {

    /**
     * 经度
     */
    private Double lon;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 发生的事件的名称
     */
    private String act;
}
