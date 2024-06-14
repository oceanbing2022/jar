package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.dto.AreaRepository;
import com.ohx.dto.WarshipRepository;
import com.ohx.entity.*;

import com.ohx.service.UserService;
import com.ohx.util.CheckMailUtil;
import com.ohx.util.DateTimeUtils;
import com.ohx.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static com.ohx.common.BizCodeEnum.*;
import static com.ohx.common.Constant.*;

/**
 * @author 贾榕福
 * @date 2022/10/28
 */

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class UserServiceController {

    @Resource
    private UserService userService;

    @Autowired
    AreaRepository areaRepository;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private WarshipRepository warshipRepository;

    /**
     * 登出
     * @param request
     * @param jsonObject
     * @Author: 贾榕福
     * @Date: 2022/10/28
     */
    @UserLoginToken
    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        //从请求头中获取json格式的param
        String param = (String)jsonObject.get("param");
        //解析params
        Base64.Decoder decoder = Base64.getDecoder();
        String param_decode = new String(decoder.decode(param));
        JSONObject param_json = JSON.parseObject(param_decode);

        //获取用户id
        String userId = param_json.getString("userId");
        //根据userId获取用户信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId)
                .last("limit 1");
        User user = userService.getOne(queryWrapper);

        log.info("user:{}",user);

        //获取accessToken
        String accessToken = request.getHeader("accessToken");
        log.info("accessToken:{}",accessToken);
        //获取refreshToken
        String refreshToken = request.getHeader("refreshToken");
        log.info("refreshToken:{}",refreshToken);

        if (accessToken == null){
            return ResponseResult.error(ACCESSTOKEN_NOT_FIND);
        }

        if (refreshToken == null){
            return ResponseResult.error(REFRESHTOKEN_NOT_FIND);
        }

        String accessTokenKey = LOGIN_USER_ACCESSTOKEN + accessToken;
        String refreshTokenKey = LOGIN_USER_REFRESHTOKEN + refreshToken;

        try {
            RedisUtils.deleteValue(accessTokenKey);
            RedisUtils.deleteValue(refreshTokenKey);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResponseResult.success();
    }


    /**
     * 获取用户信息
     * @param param
     * @Author: 贾榕福
     * @Date: 2022/11/1
     */
    @UserLoginToken
//    @PassToken
    @GetMapping("/getUserInfo")
    public ResponseResult getUserInfo(@Param("param") String param){
        //解析param
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取用户id
        String userId = param_json.getString("userId");
        log.info("userId:{}",userId);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId)
                .last("limit 1");
        User user = userService.getOne(queryWrapper);
        if (user == null){
            return ResponseResult.error(BizCodeEnum.USER_NULL);
        }else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("user", user);
            return ResponseResult.success(hashMap);
        }
    }


    /**
     * 修改用户密码
     * @param request
     * @param jsonObject
     * @Author: 贾榕福
     * @Date: 2022/11/1
     */
    @UserLoginToken
    @PostMapping("/changePassword")
    public ResponseResult changePassword(HttpServletRequest request, @RequestBody JSONObject jsonObject){
        //从请求头中获取json格式的param
        String param = (String)jsonObject.get("param");
        //解析params
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取用户邮箱
        String email = param_json.getString("email");
        log.info("email:{}",email);
        //获取用户要重置的密码
        String newPassword = param_json.getString("newPassword");
        log.info("newPassword:{}",newPassword);
        //获取验证码
        String verifyCode = param_json.getString("verifyCode");
        log.info("verifyCode:{}",verifyCode);

        //如果邮箱格式错误
        if (!CheckMailUtil.checkMail(email)) {
            return ResponseResult.error(MESSAGE_FORMAT_ERROR);
        }

        //从redis获取验证码
        String emailKey = VERIFY_CODE + email;
        String redisCode = RedisUtils.getValue(emailKey);
        log.info("redisCode:{}",redisCode);
        //将code与redis的进行比对
        if (!redisCode.equals("" + verifyCode)) {
            return ResponseResult.error(WRONG_VERIFY_CODE);
        }
        //查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email)
                .last("limit 1");
        User oldUser = userService.getOne(queryWrapper);
        String userId = oldUser.getUserId();

        //重置密码
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId)
                .set("password",newPassword);
        boolean b = userService.update(updateWrapper);
        //修改成功则删除token重新登录
        if (b == true){
            //获取accessToken
            String accessToken = request.getHeader("accessToken");
            //删除accessToken
            String accessTokenKey = LOGIN_USER_ACCESSTOKEN + accessToken;
            if (RedisUtils.hasKey(accessTokenKey)){
                RedisUtils.deleteValue(accessTokenKey);
            }

            //获取refreshToken
            String refreshToken = request.getHeader("refreshToken");
            //删除refreshToken
            String refreshTokenKey = LOGIN_USER_REFRESHTOKEN + refreshToken;
            if (RedisUtils.hasKey(refreshTokenKey)){
                RedisUtils.deleteValue(refreshTokenKey);
            }
            return ResponseResult.success();
        }else {
            return ResponseResult.error(BizCodeEnum.USER_UPDATE_FAILED);
        }
    }


    private String getRequestRegionalAis(String urlString){
        HttpURLConnection con;
        BufferedReader buffer;
        StringBuffer resultBuffer;
        String resultString = null;
        //进行HTTP请求查询
        try {
            URL url = new URL(urlString);
            //得到连接对象
            con=(HttpURLConnection) url.openConnection();
            //设置请求类型
            con.setRequestMethod("GET");
            //设置Content-Type，此处根据实际情况确定
            if(con.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream inputStream=con.getInputStream();
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                //将查询结果 转为字符串
                resultString=resultBuffer.toString();
            }
        }catch (Exception e){
            return "";
        }
        return resultString;
    }

    @UserLoginToken
//    @PassToken
    @GetMapping("/getTrackListByMMSI")
    public ResponseResult getTrackListByMMSI(@Param("param") String param){
        //解析param

        Base64.Decoder decoder=Base64.getDecoder();

        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取mmsi
        String mmsi = param_json.getString("mmsi");
        log.info("所查询的mmsi是：{}",mmsi);
        //获取mmsi
        String startdt = param_json.getString("startdt");
        log.info("所查询的开始时间是：{}",startdt);
        //获取mmsi
        String enddt = param_json.getString("enddt");
        log.info("所查询的结束时间是：{}",enddt);


        //所查询的船舶信息（mmsi，起止时间）
        StringBuilder sb = new StringBuilder();
        sb.append("{uid:\"userid\",mmsi:\"")
                .append(mmsi)
                .append("\",startdt:\"")
                .append(startdt)
                .append("\",enddt:\"")
                .append(enddt)
                .append("\"}")
        ;
        //转成字符串
        String sMmsi = String.valueOf(sb);
        byte[] bytes = sMmsi.getBytes();

        BASE64Encoder encoder = new BASE64Encoder();
        String paramAis = encoder.encode(bytes).replaceAll("\r|\n", "");

        //返回数据
        List<com.ohx.entity.Track> historyList = new LinkedList<>();

        //配置url所需要的变量
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cmd","0x0151");
        hashMap.put("param",paramAis);
        hashMap.put("time","1663907962");
        hashMap.put("api_key","yAjFdLc$Mb76@9rC");

        //ais返回参数
        String result;
        try {
            result = restTemplate.getForObject("http://oceanstellar.gogotrade.info/api?cmd={cmd}&param={param}&time={time}&api_key={api_key}",
                    String.class,hashMap);
        }catch (Exception e){
            return ResponseResult.success(historyList);
        }


        List<String> trackArray = JSONArray.parseArray(result,String.class);

        for (String s : trackArray){
            String[] split = s.split("\\|");
            Track Track = new Track();
            Track.setFlag(Integer.valueOf(split[0]));
            Track.setLongitude(Float.valueOf(split[1]));
            Track.setLatitude(Float.valueOf(split[2]));
            Track.setTime(Long.valueOf(split[3]));
            Track.setDestination(split[4]);
            Track.setSpeed(split[5]);
            Track.setIgnore0(split[6]);
            Track.setCourse(split[7]);
            Track.setArriveTime(Long.valueOf(split[8]));
            Track.setIgnore1(split[9]);
            Track.setIgnore2(split[10]);
            Track.setEventId(Integer.valueOf(split[11]));
            Track.setStatus(split[12]);
            Track.setMmsi(mmsi);
            historyList.add(Track);
        }
        return ResponseResult.success(historyList);
    }
//    /**
//     * 根据港口查询所关联的船的列表及信息
//     * @param param
//     * @Author: 贾榕福
//     * @Date: 2022/12/27
//     */
////    @PassToken
//    @UserLoginToken
//    @GetMapping("/getBoatListByPortName")
//    public ResponseResult getBoatListByPortName(@Param("param") String param){
//
//        String portName;
//        try {
//            //对参数进行base64解码
//            Base64.Decoder decoder = Base64.getDecoder();
//            String param_decode = new String(decoder.decode(param));
//            JSONObject param_json = JSON.parseObject(param_decode);
//            //获取portname
//            portName = param_json.getString("portName");
//        }catch (Exception e){
//            log.info("警告: 200001 Param error, see doc for more info");
//            return ResponseResult.error(BizCodeEnum.PARAM_ERROR2);
//        }
//        return userService.getBoatListByPortName(portName);
//    }

//    /**
//     * 获取船舶的所有的轨迹列表
//     * @param param
//     * @Author: 贾榕福
//     * @Date: 2023/1/4
//     */
//    @UserLoginToken
//    @GetMapping("/getBoatListAll")
//    public ResponseResult getBoatListAll(@Param("param") String param){
//
//        String boatName;
//        String mmsi;
//        String startTime;
//        String endTime;
//        String startdt;
//        String enddt;
//        try {
//            //对参数进行base64解码
//            Base64.Decoder decoder = Base64.getDecoder();
//            String param_decode = new String(decoder.decode(param));
//            JSONObject param_json = JSON.parseObject(param_decode);
//            //获取boatName,startTime,endTime
//            boatName = param_json.getString("boatName");
//            startTime = param_json.getString("startTime");
//            endTime = param_json.getString("endTime");
//        }catch (Exception e){
//            log.error("获取输入参数异常");
//            return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
//        }
//        //检查输入是否有效
//        if(boatName == null || startTime == null || endTime == null
//                || "".equals(boatName) || "".equals(startTime) || "".equals(endTime)){
//            log.error("输入参数无效");
//            return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
//        }
//
//        try {
//            mmsi = warshipRepository.findByname(boatName).getMmsi();
//        } catch (Exception e) {
//            return ResponseResult.error(BOAT_NOT);
//        }
//
//        try {
//            startdt = DateTimeUtils.dateToStamp(startTime);
//        } catch (ParseException e) {
//            return ResponseResult.error(START_TIME_ERROR);
//        }
//
//        try {
//            enddt = DateTimeUtils.dateToStamp(endTime);
//        } catch (ParseException e) {
//            return ResponseResult.error(END_TIME_ERROR);
//        }
//
//
//        LinkedList<Track> aisList = userService.getAISList(mmsi, startdt, enddt);
//
//        ArrayList<Neo4jTrack> neo4jList = userService.getNeo4jList(boatName,startTime,endTime);
//
//        HashMap<String,Object> listAll = new HashMap<>();
//        listAll.put("aisList",aisList);
//        listAll.put("neo4jList",neo4jList);
//        return ResponseResult.success(listAll);
//    }

//    @UserLoginToken
//    @GetMapping("/getBoatListAllByMMSI")
//    public ResponseResult getBoatListAllByMMSI(@Param("param") String param){
//
//        String mmsi;
//        String startTime;
//        String endTime;
//        String startdt;
//        String enddt;
//        try {
//            //对参数进行base64解码
//            Base64.Decoder decoder = Base64.getDecoder();
//            String param_decode = new String(decoder.decode(param));
//            JSONObject param_json = JSON.parseObject(param_decode);
//            //获取boatName,startTime,endTime
//            mmsi = param_json.getString("mmsi");
//            startTime = param_json.getString("startTime");
//            endTime = param_json.getString("endTime");
//        }catch (Exception e){
//            log.error("获取输入参数异常");
//            return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
//        }
//        //检查输入是否有效
//        if(mmsi == null || startTime == null || endTime == null
//                || "".equals(mmsi) || "".equals(startTime) || "".equals(endTime)){
//            log.error("输入参数无效");
//            return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
//        }
//
////        try {
////            mmsi = warshipRepository.findByname(boatName).getMmsi();
////        } catch (Exception e) {
////            return ResponseResult.error(BOAT_NOT);
////        }
//
//        try {
//            startdt = DateTimeUtils.dateToStamp(startTime);
//        } catch (ParseException e) {
//            return ResponseResult.error(START_TIME_ERROR);
//        }
//
//        try {
//            enddt = DateTimeUtils.dateToStamp(endTime);
//        } catch (ParseException e) {
//            return ResponseResult.error(END_TIME_ERROR);
//        }
//
//
//        LinkedList<Track> aisList = userService.getAISList(mmsi, startdt, enddt);
//
//        ArrayList<Neo4jTrack> neo4jList = userService.getNeo4jList(mmsi,startTime,endTime);
//
//        HashMap<String,Object> listAll = new HashMap<>();
//        listAll.put("aisList",aisList);
//        listAll.put("neo4jList",neo4jList);
//        return ResponseResult.success(listAll);
//    }


}
