package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.entity.ShipArchives;
import com.ohx.service.ShipArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class ShipArchivesController {

    @Resource
    private ShipArchiveService shipArchiveService;
//    @PassToken

    @UserLoginToken
    @GetMapping("/getBoatByKeyWord")
    public ResponseResult getBoatByKeyWord(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            String keyword = paramJson.getString("keyword");
            log.info("keyword" + keyword);
            List<ShipArchives> shipInfoList = shipArchiveService.getShipByKeyword(keyword);

            JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(shipInfoList));

            return ResponseResult.success(array);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }








    @UserLoginToken
    @GetMapping("/getBoatInfoByMMSI")
    public ResponseResult getBoatInfoByMMSI(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            String mmsi = paramJson.getString("mmsi");
            log.info("mmsi" + mmsi);
            QueryWrapper<ShipArchives> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("mmsi", mmsi);
            List<ShipArchives> shipArchivesList =  shipArchiveService.list(queryWrapper);


            JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(shipArchivesList));
            return ResponseResult.success(array);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }



    @UserLoginToken
    @GetMapping("/getAllType")
    public ResponseResult getAllType() {
        try {
            JSONArray list = getStringList("type_name_cn");
            return ResponseResult.success(list);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }


    private JSONArray getStringList(String column){
        try {
            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT " + column + ", count(*) FROM ship_archives group by " + column);

            JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(mapperList));

            System.out.println(list);

            List<String> stringList = new ArrayList<>();

            for(int i = 0; i < list.size(); i++){
                JSONObject object = (JSONObject)list.get(i);
                stringList.add(object.getString(column));
            }
            JSONArray list1 = JSONArray.parseArray(JSONArray.toJSONString(stringList));
            System.out.println(list1);

            return list1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
