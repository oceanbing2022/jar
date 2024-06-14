package com.ohx.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.dto.UserRecordDTO;
import com.ohx.entity.User;
import com.ohx.entity.UserRecord;
import com.ohx.service.IpLocationService;
import com.ohx.service.UserRecordService;
import com.ohx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.BeanUtils;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class UserRecordController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRecordService userRecordService;

    @PassToken
    @GetMapping("/getUserList")
    public ResponseResult getUserList() {

        List<User> userList = userService.list(null);
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < userList.size(); i++) {
            jsonArray.add(userList.get(i).getUsername());
        }

        System.out.println(jsonArray);
        return ResponseResult.success(jsonArray);
    }

//    @PassToken
//    @GetMapping("/getUserRecord")
//    public ResponseResult getUserRecord(@Param("username") String username, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd, @Param("param") String param, @Param("key") String key, @Param("timeStamp") String timeStamp, @Param("sign") String sign) {
//
//        QueryWrapper<UserRecord> queryWrapper = new QueryWrapper<>();
//        queryWrapper.between("dtime", timeStart, timeEnd);
//        if(username != null){
//            queryWrapper.eq("user_name", username);
//        }
//        List<UserRecord> list = userRecordService.list(queryWrapper);
//
//        System.out.println(list);
//        return ResponseResult.success(list);
//    }

    @PassToken
    @GetMapping("/getUserRecord")
    public ResponseResult getUserRecord(@RequestParam("username") String username,
                                        @RequestParam("timeStart") String timeStart,
                                        @RequestParam("timeEnd") String timeEnd,
                                        @RequestParam("param") String param,
                                        @RequestParam("key") String key,
                                        @RequestParam("timeStamp") String timeStamp,
                                        @RequestParam("sign") String sign) {

        QueryWrapper<UserRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("dtime", Timestamp.valueOf(timeStart), Timestamp.valueOf(timeEnd));
        if (username != null && !username.isEmpty()) {
            queryWrapper.eq("user_name", username);
        }
        List<UserRecord> list = userRecordService.list(queryWrapper);

        IpLocationService ipLocationService = new IpLocationService(new RestTemplate());

        List<UserRecordDTO> dtoList = list.stream().map(record -> {
            UserRecordDTO dto = new UserRecordDTO();
            BeanUtils.copyProperties(record, dto); // 复制属性
            dto.setLocation(ipLocationService.getIpLocation(record.getIp())); // 设置IP所属地
            return dto;
        }).collect(Collectors.toList());

        return ResponseResult.success(dtoList);
    }

    @PassToken
    @GetMapping("/getUserRecordByDay")
    public ResponseResult getUserRecordByDay(@Param("username") String username, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd, @Param("param") String param, @Param("key") String key, @Param("timeStamp") String timeStamp, @Param("sign") String sign) {

        System.out.println("timeStart" + timeStart);
        List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("select DATE_FORMAT(dtime, '%Y-%m-%d') as transDay,count(*) as transNum from user_record where dtime between {0} and {1} and user_name = {2} group by transDay order by transDay asc", timeStart, timeEnd, username);

        JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(mapperList));

        System.out.println(list);
        return ResponseResult.success(list);
    }

    @PassToken
    @GetMapping("/getUserRecordByMethod")
    public ResponseResult getUserRecordByMethod(@Param("username") String username, @Param("timeStart") String timeStart, @Param("timeEnd") String timeEnd, @Param("param") String param, @Param("key") String key, @Param("timeStamp") String timeStamp, @Param("sign") String sign) {

        System.out.println("timeStart" + timeStart);
        List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("select count(*) as num,method from user_record where dtime between {0} and {1} and user_name={2} group by method", timeStart, timeEnd, username);

        JSONArray list = JSONArray.parseArray(JSONObject.toJSONString(mapperList));

        System.out.println(list);
        return ResponseResult.success(list);
    }

}