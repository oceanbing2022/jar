package com.ohx.entity;

import lombok.Data;

/**
 * 船舶的轨迹信息
 * @Author: 贾榕福
 * @Date: 2022/12/13
 */
@Data
public class Track {
    
    //预计到达时间
    private Long arriveTime;
    //目的港
    private String course;
    //航向
    private String destination;
    //事件ID
    private Integer eventId;
    //标识，0时间点；1轨迹点
    private Integer flag;
    //速度 目的港  之间的忽略
    private String ignore0;
    //预计到达时间  事件ID  之间的忽略
    private String ignore1;
    //预计到达时间  事件ID  之间的忽略
    private String ignore2;
    //纬度
    private Float latitude;
    //经度
    private Float longitude;
    //mmsi
    private String mmsi;
    //速度
    private String speed;
    //航行状态
    private String status;
    //时间，时间戳形式
    private Long time;
}
