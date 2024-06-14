package com.ohx.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.Constant;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.dto.UserDTO;
import com.ohx.entity.User;
import com.ohx.service.UserService;
import com.ohx.util.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.ohx.common.BizCodeEnum.*;
import static com.ohx.common.Constant.*;

@RestController
@RequestMapping("/api/hsyq/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private MailUtils mailUtils;
    /**
     * 登录
     * @param jsonObject：解析后内含username和password
     * @Author: 贾榕福
     * @Date: 2022/10/25
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody JSONObject jsonObject){

        //从请求头中获取json格式的params
        String params = (String)jsonObject.get("param");
        log.info("params:{}",params);
        //base64解码
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(params));
        JSONObject param_json=JSON.parseObject(param_decode);
        //解析用户信息
        String username = param_json.getString("username");
        String password = param_json.getString("password");
        System.out.println("username: "+username);
        System.out.println("password: "+password);

        //SHA25加密
        password = SHAUtil.getSHA256WithSalt(password, "ohx");



        //从数据库查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.last("limit 1");
        User user = userService.getOne(queryWrapper);

//        log.info("数据库name:{}",user.getUsername());
//        log.info("数据库password:{}",user.getPassword());
//        log.info("前端的name:{}",username);
//        log.info("前端的password:{}",password);
        if (user == null){
            return ResponseResult.error(BizCodeEnum.LOGIN_FIND_ERROR);
        }
        System.out.println("user.getPassword(): "+user.getPassword());
        System.out.println("password: "+password);
        System.out.println(user.getPassword().equals(password));
        if(!user.getPassword().equals(password)){
            return ResponseResult.error(BizCodeEnum.LOGIN_ERROR);
        }
        //设置返回信息
        Map<String,Object> mapSuccess = new HashMap<>();
        //创建accessToken
        String accessToken = jwtUtil.createJWT(user);
        //创建refreshToken
        String refreshToken = jwtUtil.createRefreshToken(user);
        //设置过期时间
        Long LoginTime = System.currentTimeMillis();
        Long ExpireTime = LoginTime+ Constant.EXPIRE_TIME;
        log.info("系统时间:{}",LoginTime);
        log.info("过期时间:{}",ExpireTime);

        //设置用户返回信息
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        //设置redis的token的key
        String accessTokenKey = LOGIN_USER_ACCESSTOKEN + accessToken;
        String refreshTokenKey = LOGIN_USER_REFRESHTOKEN + refreshToken;
        //保存到redis中
        RedisUtils.saveValue(accessTokenKey,userDTO,Constant.EXPIRE_TIME, TimeUnit.MILLISECONDS);
        RedisUtils.saveValue(refreshTokenKey,userDTO,Constant.REFRESH_TIME, TimeUnit.MILLISECONDS);

        mapSuccess.put("accessToken",accessToken);
        mapSuccess.put("refreshToken",refreshToken);
        mapSuccess.put("user",userDTO);
        mapSuccess.put("expireTime",ExpireTime);
        return ResponseResult.success(mapSuccess);
    }

    /**
     * token刷新
     * @param jsonObject:解析后是refreshToken
     * @Author: 贾榕福
     * @Date: 2022/10/26
     */
    @PassToken
    @PostMapping("/refresh")
    public ResponseResult refresh(@RequestBody JSONObject jsonObject){
        //从请求头中获取json格式的params
        String param = (String)jsonObject.get("param");
        //解析param
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        JSONObject param_json=JSON.parseObject(param_decode);
        //获取refreshToken
        String refreshToken = param_json.getString("refreshToken");
        log.info("refreshToken:{}",refreshToken);
        if (refreshToken == null){
            return ResponseResult.error(REFRESHTOKEN_NOT_FIND);
        }
        //存储返回值数据
        HashMap<String, Object> map = new HashMap<>();
        //解析token
        Claims claims = jwtUtil.parseToken(refreshToken);
        Date expiration = claims.getExpiration();
        log.info("data:{}",expiration);
        Date nowDate = new Date();
        log.info("nowDate:{}",nowDate);

        //判断refreshToken是否过期
        int compareTo = expiration.compareTo(nowDate);
        if (compareTo >0){
            String id = claims.getId();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",id)
                    .last("limit 1");
            User user = userService.getOne(queryWrapper);
            log.info("user:{}",user);
            String newAccessToken = jwtUtil.createJWT(user);
            String newRefreshToken = jwtUtil.createRefreshToken(user);
            Long LoginTime = System.currentTimeMillis();
            Long ExpireTime = LoginTime+Constant.EXPIRE_TIME;
            map.put("accessToken",newAccessToken);
            map.put("refreshToken",newRefreshToken);
            map.put("expireTime",ExpireTime);

            String refreshTokenKey = LOGIN_USER_REFRESHTOKEN + refreshToken;
            RedisUtils.deleteValue(refreshTokenKey);

            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);

            String newAccessTokenKey = LOGIN_USER_ACCESSTOKEN + newAccessToken;
            String newRefreshTokenKey = LOGIN_USER_REFRESHTOKEN + newRefreshToken;
            RedisUtils.saveValue(newAccessTokenKey,userDTO,Constant.EXPIRE_TIME, TimeUnit.MILLISECONDS);
            RedisUtils.saveValue(newRefreshTokenKey,userDTO,Constant.REFRESH_TIME, TimeUnit.MILLISECONDS);

            log.info("newAccessToken:{}",newAccessToken);
            log.info("newRefreshToken:{}",newRefreshToken);
            return ResponseResult.success(map);
        }else {
            return ResponseResult.error(BizCodeEnum.REFRESHTOKEN_TIMEOUT);
        }
    }


    /**
     * 验证邮箱是否重复
     * @Author: 贾榕福
     * @Date: 2022/11/21
     */
    @PassToken
    @PostMapping("/checkEmail")
    public ResponseResult checkEmailRepeat(@RequestBody JSONObject jsonObject) {

        //从请求头中获取json格式的params
        String params = (String)jsonObject.get("param");
        //解析params
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(params));
        JSONObject param_json=JSON.parseObject(param_decode);
        //获取email
        String email = param_json.getString("email");
        log.info("email:{}",email);

        //检查邮箱格式是否正确
        if (!CheckMailUtil.checkMail(email)) {
            return ResponseResult.error(MESSAGE_FORMAT_ERROR);
        }
        //检测邮箱是否重复
        //重复
        if (userService.checkEmaillRepeated(email)) {
            return ResponseResult.success(USER_EMAIL_REPEATED);
        }
        return ResponseResult.error(USER_EMAIL_NOT_REPEATED);
    }


    /**
     * 发送注册验证码
     * @param
     * @Author: 贾榕福
     * @Date: 2022/11/21
     */
    @PassToken
    @GetMapping("/sendEmailCode")
    public ResponseResult sendSignUpCode(@Param("param") String param) {

        //解析param
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        JSONObject param_json=JSON.parseObject(param_decode);
        //获取toEamil
        String toEamil = param_json.getString("toEamil");
        log.info("toEamil:{}",toEamil);
        //生成验证码
        String rate = RandomUtil.randomNumbers(6);
        log.info("验证码：{}",rate);
        //存入redis
        String emailKey = VERIFY_CODE + toEamil;
        RedisUtils.saveValue(emailKey,rate,EMAIL_TIME,TimeUnit.MILLISECONDS);
        //发送给用户的文本
        String content =
                "你好，\n" + "\t您的验证码是：\n" + rate;
        //发送邮件
        try {
            mailUtils.sendMail(toEamil,content,TITLE);
        } catch (Exception e) {
            return ResponseResult.error(FAIL_SEND);
        }
        return ResponseResult.success();
    }


    /**
     * 用户注册
     * @Author: 贾榕福
     * @Date: 2022/11/21
     */
    @PassToken
    @PostMapping("/register")
    public ResponseResult UserSignUp(@RequestBody JSONObject jsonObject) {

        //从请求头中获取json格式的param
        String param = (String)jsonObject.get("param");
        //解析param
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        JSONObject param_json=JSON.parseObject(param_decode);

        //获取username
        String username = param_json.getString("username");
        log.info("username:{}",username);
        //获取password
        String password = param_json.getString("password");
        log.info("password:{}",password);
        //获取email
        String email = param_json.getString("email");
        log.info("email:{}",email);
        //获取phone
        String phone = param_json.getString("phone");
        log.info("phone:{}",phone);
        //获取code
        String verifyCode = param_json.getString("verifyCode");
        log.info("verifyCode:{}",verifyCode);

        //检测邮箱格式是否正确
        if (!CheckMailUtil.checkMail(email)) {
            return ResponseResult.error(MESSAGE_FORMAT_ERROR);
        }
        //检测是否邮箱重复注册
        if (userService.checkEmaillRepeated(email)) {
            return ResponseResult.error(USER_EMAIL_REPEATED);
        }
        //获取验证码
        String emailKey = VERIFY_CODE + email;
        String redisCode = RedisUtils.getValue(emailKey);
        log.info("redisCode:{}",redisCode);
        //将code与redis的进行比对
        if (!redisCode.equals("" + verifyCode)) {
            return ResponseResult.error(WRONG_VERIFY_CODE);
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setEmail(email);
        //注册用户
        if (userService.insertUser(user)) {
            HashMap<String,String> map = new HashMap<>();
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email",email)
                            .last("limit 1");
            User newUser = userService.getOne(queryWrapper);
            map.put("userId",newUser.getUserId());
            map.put("username",username);
            map.put("email",email);
            return ResponseResult.success(map);
        }
        return ResponseResult.error(USER_SIGNUP_FAILED);
    }

}
