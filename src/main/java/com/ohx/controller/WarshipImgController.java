package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.entity.WarshipImg;
import com.ohx.service.WarshipImgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import static com.ohx.common.Constant.SERVER_PREFIX;

/**
 * @author 贾榕福
 * @date 2022/11/2
 */
@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class WarshipImgController {
    
    @Resource
    WarshipImgService warshipService;
    
    /**
     * 根据传入的船舶关键字（舷号）查询与之关联的图片url列表
     * @param param
     * @Author: 贾榕福
     * @Date: 2022/11/2
     */
    @UserLoginToken
//    @PassToken
    @GetMapping("/getBoatURL")
    public ResponseResult getBoatURL(@Param("param") String param){
        
        //解析param
        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);
        
        //获取船舶舷号
        JSONArray boatKeyWordList = param_json.getJSONArray("boatKeyWordList");
        log.info("boatKeyWordList:{}",boatKeyWordList);
        //获取pageSize,分页查询中，每页的数量
        int pageSize = Integer.parseInt(param_json.getString("pageSize"));
        //获取pageNum,分页查询中，页面的编码，页码从1开始
        int pageNum = Integer.parseInt(param_json.getString("pageNum"));

        
        
        int boatListSize = boatKeyWordList.size();
        //构造分页构造器
        Page page = new Page(pageNum, pageSize);
        
        if (boatListSize == 1){
            String boatKeyWord = (String) boatKeyWordList.get(0);
            String boatKeyWordLowAndTrim;
            if(boatKeyWord.contains("-")){
                boatKeyWordLowAndTrim = boatKeyWord.replace("-","").toLowerCase().trim();
            }else if (boatKeyWord.contains(" ")){
                boatKeyWordLowAndTrim = boatKeyWord.replace(" ","").toLowerCase().trim();
            }else {
                boatKeyWordLowAndTrim = boatKeyWord.toLowerCase().trim();

            }
            //按舷号查询
            QueryWrapper<WarshipImg> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(StringUtils.isNotEmpty(boatKeyWordLowAndTrim),"tweet_context",boatKeyWord);
            //执行查询
            warshipService.page(page,queryWrapper);
        }

        if (boatListSize == 2){
            //获取舷号
            String boatKeyWord = (String) boatKeyWordList.get(0);
            //删除"-"、空格及转换成小写
            String boatKeyWordLowAndTrim;
            if(boatKeyWord.contains("-")){
                boatKeyWordLowAndTrim = boatKeyWord.replace("-","").toLowerCase().trim();
            }else if (boatKeyWord.contains(" ")){
                boatKeyWordLowAndTrim = boatKeyWord.replace(" ","").toLowerCase().trim();
            }else {
                boatKeyWordLowAndTrim = boatKeyWord.toLowerCase().trim();
            }
            log.info("舷号：" + boatKeyWordLowAndTrim);
            log.info("舷号是否不空：" + StringUtils.isNotEmpty(boatKeyWordLowAndTrim));
            //获取船名
            String boatNameKeyWord = (String) boatKeyWordList.get(1);
            QueryWrapper<WarshipImg> queryWrapper = new QueryWrapper<>();

            if (boatKeyWordLowAndTrim != null && StringUtils.isNotEmpty(boatKeyWordLowAndTrim) && !boatKeyWordLowAndTrim.equals(" ")){
                //按舷号查询
                queryWrapper.like(StringUtils.isNotEmpty(boatKeyWordLowAndTrim) || StringUtils.isNotEmpty(boatNameKeyWord),"tweet_context",boatKeyWordLowAndTrim);
            }else {
                boatNameKeyWord = filter(boatNameKeyWord);
                log.info("船名：", boatNameKeyWord);
                //按船名查询
                queryWrapper.like(StringUtils.isNotEmpty(boatKeyWordLowAndTrim) || StringUtils.isNotEmpty(boatNameKeyWord),"tweet_context",boatNameKeyWord);
            }
            //执行查询
            warshipService.page(page,queryWrapper);
        }
        
        //currentLength当前图片路径列表长度
        int currentLength = 0;
        //总记录数
        int total = (int) page.getTotal();
        log.info("total:{}",total);
        //获取总页数
        int pages = (int) page.getPages();
        //获取每页条数
        int size = (int) page.getSize();
        //获取当前页数
        int current = (int) page.getCurrent();
        

        List<String> records = new ArrayList<>();

        if (current == pages){
            currentLength = total - (current - 1) * size;

        }else {
            currentLength = size;
        }
        
        if (total != 0) {
            //路径
            for (int i = 0; i < currentLength; i++) {
                WarshipImg record = (WarshipImg) page.getRecords().get(i);
                log.info("record:{}", record);
                log.info("filePath:{}", record.getFilePath());
                
                //图片请求路径
                StringBuilder sb = new StringBuilder();
                sb.append(SERVER_PREFIX)
                        .append(record.getFilePath());
                String sbFilePath = sb.toString();
                String sFPH = sbFilePath.replace("/home", "");
                String sFPHT = null;
                if (sFPH.contains("/twitter")){
                    sFPHT = sFPH.replaceFirst("/twitter", "");
                }
                if (sFPH.contains("/rimpeace")){
                    sFPHT = sFPH.replaceFirst("/rimpeace", "");
                }

                if (sFPH.contains("/bing")){
                    sFPHT = sFPH.replaceFirst("/bing", "");
                }

                if (sFPH.contains("/google")){
                    sFPHT = sFPH.replaceFirst("/google", "");
                }

                if (sFPH.contains("/alamy")){
                    sFPHT = sFPH.replaceFirst("/alamy", "");
                }

                
                //拼接缩略图前缀c-
                String[] split = sFPHT.split("/");
                String splitLast = split[split.length - 1];
                String cRecords = sFPHT.replace(splitLast, "c-" + splitLast);
                records.add(cRecords);
            }
        }
        //返回结果
        HashMap<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("pages",pages);
        map.put("size",size);
        map.put("current",current);
        map.put("currentLength",currentLength);
        map.put("records",records);


        return ResponseResult.success(map);
    }

    @PassToken
    @GetMapping("/getTestBoatURL")
    public ResponseResult getTestBoatURL(@Param("param") String param){
        String a;

        
        List<String> list = new ArrayList<>();

        for (int i = 1; i <= 20; i++) {
            a = "59.110.236.224:8082/img/" + i +".png";
            list.add(a);
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("total",4);
        map.put("pages",2);
        map.put("size",3);
        map.put("current",1);
        map.put("currentLength",3);
        map.put("records",list);
        return ResponseResult.success(map);
    }

    private String filter(String str) throws PatternSyntaxException {
// 清除掉所有特殊字符
        str = str.replaceAll("[^a-zA-Z0-9]", "");
        return str.replace(" ", "");
    }

}