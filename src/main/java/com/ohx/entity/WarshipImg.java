package com.ohx.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 贾榕福
 * @date 2022/11/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarshipImg implements Serializable {
    
    
    //主键
    @TableId(value = "id")
    private Integer id;
    //推特id
    private String tweetId;
    //推特舷号
    private String tweetContext;
    //图片路径
    private String filePath;
    //推特时间
    private int tweetTime;
}
