//package com.ohx.controller;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.ohx.common.PassToken;
//import com.ohx.common.ResponseResult;
//import com.ohx.common.UserLoginToken;
//import com.ohx.entity.ShipInfo;
//import com.ohx.service.ShipInfoService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.annotations.Param;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.Base64;
//import java.util.List;
//
//import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;
//
//@RestController
//@RequestMapping("/api/hsyq/service")
//@Slf4j
//public class ShipInfoController {
//
//    @Resource
//    private ShipInfoService shipInfoService;
//
//    //测试的时候不想弄token可以去掉 @UserLoginToken然后加上 @PassToken
//
//    @UserLoginToken
////    @PassToken //最后记得删掉哦
//    @GetMapping("/getShipInfo")
//    public ResponseResult getShipInfo(@RequestParam("param") String param) {
//
//        try {
//            String decodedParam = new String(Base64.getDecoder().decode(param));
//            JSONObject param_json= JSON.parseObject(decodedParam);
//            String keyword = param_json.getString("keyword");
//            log.info("keyword" + keyword);
//            List<ShipInfo> shipInfoList = shipInfoService.getShipInfoByParameter(keyword);
//            return ResponseResult.success(shipInfoList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseResult.error(PARAMETER_ERROR);
//        }
//
//    }
//
//    @UserLoginToken
////    @PassToken
//    @GetMapping("/getShipInfoByID")
//    public ResponseResult getShipInfoByID(@Param("param") String param){
//        Base64.Decoder decoder=Base64.getDecoder();
//        String param_decode=new String(decoder.decode(param));
//        log.info("param_decode:{}",param_decode);
//        try{
//            JSONObject param_json= JSON.parseObject(param_decode);
//            String ID = param_json.getString("id");
//            log.info("ID" + ID);
//            System.out.println("ID" + ID);
//            QueryWrapper<ShipInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("id", ID);
//            List<ShipInfo> shipInfoList = shipInfoService.list(queryWrapper);
//            return ResponseResult.success(shipInfoList);
//        }catch (Exception e){
//            e.printStackTrace();
//            return ResponseResult.error(PARAMETER_ERROR);
//        }
//
//    }
//}
