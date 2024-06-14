package com.ohx.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    //主键
    @TableId(value = "id")
    private Integer id;
    //用户Id
    private String userId;
    //用户名
    private String username;
    //密码
    private String password;
    //手机号
    private String phone;
    //昵称
    private String nickname;
    //公司
    private String com;
    //部门
    private String department;
    //邮箱
    private String email;
    //类型
    private String type;
    //权限(0:owner 1:admin 2:user)
    private int permission;
    
}
