package com.ohx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userName;

    private String ip;

    private String method;

    private Timestamp dtime;


}
