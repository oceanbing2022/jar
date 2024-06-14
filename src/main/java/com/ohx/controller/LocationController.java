package com.ohx.controller;

import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.entity.Location;
import com.ohx.entity.ShipInfoMin;
import com.ohx.service.LocationService;
import com.ohx.service.ShipInfoMinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;
import javax.annotation.Resource;
import java.util.Base64;
import java.util.List;
@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Resource
    private ShipInfoMinService shipInfoMinService;
//    @PassToken
    @UserLoginToken
    @GetMapping("/getAllPortList")
    public ResponseResult getAllPortList(){
        List<Location> list = locationService.list(null);
        return ResponseResult.success(list);
    }

//    @PassToken
    @UserLoginToken
    @GetMapping("/getLocationBoat")
    public ResponseResult getLocation(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            String keyword = paramJson.getString("keyword");
            log.info("keyword" + keyword);
            List<Location> locationList = locationService.getLocationByKeyword(keyword);
            List<ShipInfoMin> shipInfoList = shipInfoMinService.getShipInfoMinByParameter(keyword);
            // Create a map to hold both lists
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("shipInfoList", shipInfoList);
            responseData.put("locationList", locationList);

            return ResponseResult.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }

    @PassToken
//    @UserLoginToken
    @GetMapping("/getLocation")
    public ResponseResult getLoca(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            String keyword = paramJson.getString("keyword");
            log.info("keyword" + keyword);
            List<Location> locationList = locationService.getLocationByKeyword(keyword);
//            List<ShipInfo> shipInfoList = shipInfoService.getShipInfoByParameter(keyword);
            // Create a map to hold both lists
            Map<String, Object> responseData = new HashMap<>();
//            responseData.put("shipInfoList", shipInfoList);
            responseData.put("locationList", locationList);

            return ResponseResult.success(responseData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }
}
