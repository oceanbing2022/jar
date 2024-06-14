package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.entity.GroupRegion;
import com.ohx.entity.Region;
import com.ohx.entity.ShipArchives;
import com.ohx.entity.TableAis;
import com.ohx.service.GroupRegionService;
import com.ohx.service.RegionService;
import com.ohx.service.ShipArchiveService;
import com.ohx.service.TableAisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j
public class LocationAisController {

    @Resource
    private GroupRegionService groupRegionService;

    @Resource
    private ShipArchiveService shipArchiveService;

    @Resource
    private RegionService regionService;
//    @PassToken

    @Resource
    private TableAisService tableAisService;





    @UserLoginToken
    @PostMapping("/addNewRegionGroup")
    public ResponseResult addNewRegionGroup(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String groupName = param_json.getString("groupName");
            String userId = param_json.getString("userId");
            System.out.println("groupName: " + groupName);
            System.out.println("userId: " + userId);


            GroupRegion groupRegion = new GroupRegion();
            groupRegion.setGroupName(groupName);
            groupRegion.setUserId(userId);
            groupRegionService.save(groupRegion);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }

    @UserLoginToken
    @PostMapping("/addNewRegion")
    public ResponseResult addNewRegion(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String regionName = param_json.getString("regionName");
            Integer groupId = param_json.getInteger("groupId");
            Integer state = param_json.getInteger("state");
            String minLon = param_json.getString("minLon");
            String maxLon = param_json.getString("maxLon");
            String minLat = param_json.getString("minLat");
            String maxLat = param_json.getString("maxLat");
            String conditions = param_json.getString("conditions");

            Region region = new Region();
            region.setRegionName(regionName);
            region.setGroupId(groupId);
            region.setState(state);
            region.setMinLon(minLon);
            region.setMaxLon(maxLon);
            region.setMinLat(minLat);
            region.setMaxLat(maxLat);
            region.setConditions(conditions);
            regionService.save(region);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }

    @UserLoginToken
    @GetMapping("/getGroupByUserId")
    public ResponseResult getGroupByUserId(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            String userId = paramJson.getString("userId");
            log.info("userId" + userId);
            QueryWrapper<GroupRegion> groupRegionQueryWrapper = new QueryWrapper<>();
            groupRegionQueryWrapper.eq("user_id", userId);
            List<GroupRegion> listGroupRegion = groupRegionService.list(groupRegionQueryWrapper);

            JSONArray jsonList = JSONArray.parseArray(JSONArray.toJSONString(listGroupRegion));

            log.info(jsonList.toJSONString());

            return ResponseResult.success(jsonList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }





    @UserLoginToken
    @GetMapping("/getRegionByGroupId")
    public ResponseResult getRegionByGroupId(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            Integer groupId = paramJson.getInteger("groupId");
            log.info("groupId" + groupId);
            QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
            regionQueryWrapper.eq("group_id", groupId);
            List<Region> listGroupRegion = regionService.list(regionQueryWrapper);

            JSONArray jsonList = JSONArray.parseArray(JSONArray.toJSONString(listGroupRegion));

            log.info(jsonList.toJSONString());

            return ResponseResult.success(jsonList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }

    @UserLoginToken
    @GetMapping("/getRegionInfoById")
    public ResponseResult getRegionInfoById(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            Integer regionId = paramJson.getInteger("regionId");
            log.info("regionId" + regionId);
            QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
            regionQueryWrapper.eq("region_id", regionId);

            Region object = regionService.getOne(regionQueryWrapper);

            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));

            log.info(jsonObject.toJSONString());

            return ResponseResult.success(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }

    @UserLoginToken
    @GetMapping("/getAisStatById")
    public ResponseResult getAisStatById(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            Integer regionId = paramJson.getInteger("regionId");
            String startT = paramJson.getString("start");
            String endT = paramJson.getString("end");
            log.info("regionId" + regionId);

            QueryWrapper<TableAis> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("region_id", regionId);
            queryWrapper.between("time", startT, endT);
            List<TableAis> objectList = tableAisService.list(queryWrapper);

            JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(objectList));

            log.info(list.toJSONString());

            return ResponseResult.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }
    }


    @UserLoginToken
    @GetMapping("/getAisStatByIdWithShipInfo")
    public ResponseResult getAisStatByIdWithShipInfo(@RequestParam("param") String param) {
        try {
            String decodedParam = new String(Base64.getDecoder().decode(param));
            JSONObject paramJson = JSON.parseObject(decodedParam);
            Integer regionID = paramJson.getInteger("regionId");

            log.info("regionId" + regionID);

//            QueryWrapper<TableAis> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("region_id", regionId);
//            List<TableAis> objectList = tableAisService.list(queryWrapper);
//
//            JSONArray jsonArray = new JSONArray();
//
//            for (int i = 0; i < objectList.size(); i++) {
//                TableAis itemI = objectList.get(i);
//                QueryWrapper<ShipArchives> queryWrappers = new QueryWrapper<>();
//                queryWrappers.eq("mmsi", itemI.getMmsi());
//                List<ShipArchives> shipArchivesList = shipArchiveService.list(queryWrappers);
//                JSONObject objectTableAis = JSONObject.parseObject(JSONObject.toJSONString(itemI));
//                JSONObject objectShipArchives = JSONObject.parseObject(JSONObject.toJSONString(shipArchivesList.get(0)));
//                objectTableAis.putAll(objectShipArchives);
//                jsonArray.add(objectTableAis);
//            }
//
//            log.info(jsonArray.toJSONString());









            String start = paramJson.getString("start");
            String end = paramJson.getString("end");
            List<Map<String, Object>> mapperList = SqlRunner.db().selectList("SELECT  a.id, a.mmsi, a.region_id,a.ais_status, a.time, a.lon, a.lat, b.imo," +
                    "  b.call_sign, b.name_en, b.name_cn, b.flag, b.total_ton, b.load_ton, b.build_date, b.type_code, b.type_name_en, b.type_name_cn, b.status_code, b.status_string, b.length, b.wide, b.depth, b.freeboard, b.design_speed, b.design_draft, b.main_engine_speed\n" +
                    "FROM table_ais a LEFT  JOIN ship_archives b ON a.mmsi = b.mmsi  where a.region_id = " + regionID + " and time between " + start + " and" + " " + end);

            JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(mapperList));

            System.out.println(list);

            return ResponseResult.success(list);

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseResult.error(PARAMETER_ERROR);
            }
        }







    @UserLoginToken
    @PostMapping("/renameRegionGroup")
    public ResponseResult renameRegionGroup(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String groupName = param_json.getString("groupName");
            String groupId = param_json.getString("groupId");
            System.out.println("groupName: " + groupName);
            System.out.println("groupId: " + groupId);


            UpdateWrapper<GroupRegion> wrapper = new UpdateWrapper<>();
            wrapper.eq("group_id", groupId);
            wrapper.set("group_name", groupName);

            groupRegionService.update(wrapper);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }



    @UserLoginToken
    @PostMapping("/renameRegion")
    public ResponseResult renameRegion(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String regionName = param_json.getString("regionName");
            String regionId = param_json.getString("regionId");
            System.out.println("groupName: " + regionName);
            System.out.println("regionId: " + regionId);


            UpdateWrapper<Region> wrapper = new UpdateWrapper<>();
            wrapper.eq("region_id", regionId);
            wrapper.set("region_name", regionName);

            regionService.update(wrapper);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }




    @UserLoginToken
    @PostMapping("/updateRegionState")
    public ResponseResult updateRegionState(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            Integer state = param_json.getInteger("state");
            String regionId = param_json.getString("regionId");
            System.out.println("state: " + state);
            System.out.println("regionId: " + regionId);


            UpdateWrapper<Region> wrapper = new UpdateWrapper<>();
            wrapper.eq("region_id", 2);
            wrapper.set("state", state);

            regionService.update(wrapper);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }


    @UserLoginToken
    @PostMapping("/deleteGroupRegion")
    public ResponseResult deleteGroupRegion(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String groupId = param_json.getString("groupId");
            System.out.println("groupId: " + groupId);

            QueryWrapper<GroupRegion> groupRegionQueryWrapper = new QueryWrapper<>();
            groupRegionQueryWrapper.eq("group_id", groupId);

            groupRegionService.remove(groupRegionQueryWrapper);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }




    @UserLoginToken
    @PostMapping("/deleteRegionById")
    public ResponseResult deleteRegionById(@RequestBody JSONObject jsonObject){

        try {
            //从请求头中获取json格式的params
            String params = (String)jsonObject.get("param");
            log.info("params:{}",params);
            //base64解码
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(params));
            JSONObject param_json=JSON.parseObject(param_decode);
            //解析用户信息
            String regionId = param_json.getString("regionId");
            System.out.println("regionId: " + regionId);

            QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
            regionQueryWrapper.eq("region_id", regionId);

            regionService.remove(regionQueryWrapper);

            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(PARAMETER_ERROR);
        }

    }

}
