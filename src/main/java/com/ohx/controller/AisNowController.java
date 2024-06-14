package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;

import com.ohx.service.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;
/**
 * @author 李淑婧
 * @date 2024/1/19
 */

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class AisNowController {

    @Resource
    private ShipInfoMinService shipInfoMinService;
    @Resource
    private RestTemplate restTemplate;



    /**
     * 根据单个mmsi获取实时AIS信息，全部返回（未细化）
     * @param param
     * @Author: 李淑婧
     * @Date: 2024/1/16
     */
    @UserLoginToken
//    @PassToken
    @GetMapping("/getAISnowByMMSI")
    public ResponseResult getAISnowByMMSI(@Param("param") String param){
        //解析param

        Base64.Decoder decoder=Base64.getDecoder();

        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取mmsi
        String mmsi = param_json.getString("mmsi");
        log.info("所查询的mmsi是：{}",mmsi);


        String p="{mmsi:\""+ mmsi  +  "\"}";
        log.info(p);
        //将参数进行base64编码
        Base64.Encoder encoder1=Base64.getEncoder();
        String P=encoder1.encodeToString(p.getBytes());
        //配置url所需要的变量
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cmd","0x5101");
        hashMap.put("param",P);
        hashMap.put("time","1663907962");
        hashMap.put("api_key","yAjFdLc$Mb76@9rC");

        //ais返回参数，查api
        String result;
        try {
            result = restTemplate.getForObject("http://oceanstellar.gogotrade.info/api?cmd={cmd}&param={param}&time=1663907962&api_key=yAjFdLc$Mb76@9rC",
                    String.class,hashMap);
            System.out.println(result);

            JSONObject apiResponse = JSON.parseObject(result);
// 将API响应转换为类似shipInfoList的格式
            JSONObject newRestResult = new JSONObject();
            newRestResult.put("mmsi", apiResponse.getString("mmsi"));
            newRestResult.put("time", apiResponse.getString("time"));
            newRestResult.put("x", apiResponse.getString("x"));
            newRestResult.put("y", apiResponse.getString("y"));
            newRestResult.put("cog", apiResponse.getString("cog"));
            newRestResult.put("true_head", apiResponse.getString("true_head"));
            newRestResult.put("sog", apiResponse.getString("sog"));
            newRestResult.put("nav_status", apiResponse.getString("nav_status"));
            newRestResult.put("srcid", apiResponse.getString("srcid"));
            newRestResult.put("shipname", apiResponse.getString("shipname"));
            newRestResult.put("shipnamecn", apiResponse.getString("shipnamecn"));
            newRestResult.put("ship_type", apiResponse.getString("ship_type"));
            newRestResult.put("imo", apiResponse.getString("imo"));
            newRestResult.put("eta", apiResponse.getString("eta"));
            newRestResult.put("dest", apiResponse.getString("dest"));
            newRestResult.put("dest_en", apiResponse.getString("dest_en"));
            newRestResult.put("dest_cn", apiResponse.getString("dest_cn"));
            newRestResult.put("destportid", apiResponse.getString("destportid"));
            newRestResult.put("draught", apiResponse.getString("draught"));
            newRestResult.put("length", apiResponse.getString("length"));
            newRestResult.put("width", apiResponse.getString("width"));
            newRestResult.put("callsign", apiResponse.getString("callsign"));
            newRestResult.put("flag", apiResponse.getString("flag"));
            newRestResult.put("match", apiResponse.getString("match"));
            newRestResult.put("danger", apiResponse.getString("danger"));
            newRestResult.put("sbdlb", apiResponse.getString("sbdlb"));


            return ResponseResult.success(newRestResult);
        }catch (Exception e){
            return ResponseResult.success("fail");
        }
    }

    /**
     * 根据单个mmsi获取实时AIS信息，以及数据库中的船舶信息全部返回（未细化）
     * @param param
     * @Author: 李淑婧
     * @Date: 2024/1/16
     */
    @UserLoginToken
//    @PassToken
    @GetMapping("/getAISnowByMMSI2")
    public ResponseResult getAISnowByMMSI2(@Param("param") String param){
        //解析param

        Base64.Decoder decoder=Base64.getDecoder();

        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取mmsi
        String mmsi = param_json.getString("mmsi");
        log.info("所查询的mmsi是：{}",mmsi);


        String p="{mmsi:\""+ mmsi  +  "\"}";
        log.info(p);
        //将参数进行base64编码
        Base64.Encoder encoder1=Base64.getEncoder();
        String P=encoder1.encodeToString(p.getBytes());
        //配置url所需要的变量
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cmd","0x5101");
        hashMap.put("param",P);
        hashMap.put("time","1663907962");
        hashMap.put("api_key","yAjFdLc$Mb76@9rC");

        //ais返回参数，查api
        String result;
        try {
            result = restTemplate.getForObject("http://oceanstellar.gogotrade.info/api?cmd={cmd}&param={param}&time=1663907962&api_key=yAjFdLc$Mb76@9rC",
                    String.class,hashMap);
            System.out.println(result);

            JSONObject apiResponse = JSON.parseObject(result);
// 将API响应转换为类似shipInfoList的格式
            JSONObject newRestResult = new JSONObject();
            newRestResult.put("mmsi", apiResponse.getString("mmsi"));
            newRestResult.put("time", apiResponse.getString("time"));
            newRestResult.put("x", apiResponse.getString("x"));
            newRestResult.put("y", apiResponse.getString("y"));
            newRestResult.put("cog", apiResponse.getString("cog"));
            newRestResult.put("true_head", apiResponse.getString("true_head"));
            newRestResult.put("sog", apiResponse.getString("sog"));
            newRestResult.put("nav_status", apiResponse.getString("nav_status"));
            newRestResult.put("srcid", apiResponse.getString("srcid"));
            newRestResult.put("shipname", apiResponse.getString("shipname"));
            newRestResult.put("shipnamecn", apiResponse.getString("shipnamecn"));
            newRestResult.put("ship_type", apiResponse.getString("ship_type"));
            newRestResult.put("imo", apiResponse.getString("imo"));
            newRestResult.put("eta", apiResponse.getString("eta"));
            newRestResult.put("dest", apiResponse.getString("dest"));
            newRestResult.put("dest_en", apiResponse.getString("dest_en"));
            newRestResult.put("dest_cn", apiResponse.getString("dest_cn"));
            newRestResult.put("destportid", apiResponse.getString("destportid"));
            newRestResult.put("draught", apiResponse.getString("draught"));
            newRestResult.put("length", apiResponse.getString("length"));
            newRestResult.put("width", apiResponse.getString("width"));
            newRestResult.put("callsign", apiResponse.getString("callsign"));
            newRestResult.put("flag", apiResponse.getString("flag"));
            newRestResult.put("match", apiResponse.getString("match"));
            newRestResult.put("danger", apiResponse.getString("danger"));
            newRestResult.put("sbdlb", apiResponse.getString("sbdlb"));

            QueryWrapper<ShipInfoMin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("MMSI", mmsi);
            List<ShipInfoMin> shipInfoList = shipInfoMinService.list(queryWrapper);
            Map<String, Object> combinedResult = new HashMap<>();
            combinedResult.put("shipInfoList", shipInfoList);
            combinedResult.put("restResult", newRestResult);

            return ResponseResult.success(combinedResult);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }



    /**
     * 根据多个mmsi获取实时AIS信息，全部返回（未细化）
     * @param param
     * @Author: 李淑婧
     * @Date: 2024/1/16
     */
    @UserLoginToken
//    @PassToken
    @GetMapping("/getAISnowBymutiMMSI")
    public ResponseResult getAISnowBymutiMMSI(@Param("param") String param){
        //解析param

        Base64.Decoder decoder=Base64.getDecoder();

        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        //获取mmsi
        String mmsi = param_json.getString("mmsi");
        log.info("所查询的mmsi是：{}",mmsi);


        String p="{mmsis:\""+ mmsi  +  "\"}";
        log.info(p);
        //将参数进行base64编码
        Base64.Encoder encoder1=Base64.getEncoder();
        String P=encoder1.encodeToString(p.getBytes());
        //配置url所需要的变量
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("cmd","0x5102");
        hashMap.put("param",P);
        hashMap.put("time","1663907962");
        hashMap.put("api_key","yAjFdLc$Mb76@9rC");

        //ais返回参数
        String result;
        try {
            result = restTemplate.getForObject("http://oceanstellar.gogotrade.info/api?cmd={cmd}&param={param}",
                    String.class,hashMap);


            JSONArray apiResponseArray = JSON.parseArray(result);

            // 构建新的JSON结构
            JSONArray newResultArray = new JSONArray();
            for (int i = 0; i < apiResponseArray.size(); i++) {
                JSONObject ship = apiResponseArray.getJSONObject(i);
                JSONObject newShip = new JSONObject();
                newShip.put("mmsi", ship.getString("mmsi"));
                newShip.put("time", ship.getString("time"));
                newShip.put("x", ship.getString("x"));
                newShip.put("y", ship.getString("y"));
                newShip.put("cog", ship.getString("cog"));
                newShip.put("true_head", ship.getString("true_head"));
                newShip.put("sog", ship.getString("sog"));
                newShip.put("status", ship.getString("status"));
                newShip.put("srcid", ship.getString("srcid"));
                newShip.put("name", ship.getString("name"));
                newShip.put("shipnamecn", ship.getString("shipnamecn"));
                newShip.put("shiptp", ship.getString("shiptp"));
                newShip.put("imo", ship.getString("imo"));
                newShip.put("eta", ship.getString("eta"));
                newShip.put("dest", ship.getString("dest"));
                newShip.put("portId", ship.getString("portId"));
                newShip.put("dest_en", ship.getString("dest_en"));
                newShip.put("dest_cn", ship.getString("dest_cn"));
                newShip.put("draft", ship.getString("draft"));
                newShip.put("length", ship.getString("length"));
                newShip.put("width", ship.getString("width"));
                newShip.put("callsign", ship.getString("callsign"));
                newShip.put("flag", ship.getString("flag"));
                newShip.put("build", ship.getString("build"));
                newShip.put("match", ship.getString("match"));
                newShip.put("danger", ship.getString("danger"));
                newShip.put("sbdlb", ship.getString("sbdlb"));
                newResultArray.add(newShip);
            }

            return ResponseResult.success(newResultArray);

        }catch (Exception e){
            return ResponseResult.success("fail");
        }
    }
}
