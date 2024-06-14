package com.ohx.dto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserRecordDTO {
    private Integer id;
    private String userName;
    private String ip;
    private String method;
    private Timestamp dtime;
    private String location; // 用于存储IP的所属地

    // 构造函数、getters 和 setters
}
