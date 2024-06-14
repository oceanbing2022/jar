package com.ohx;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.ohx.entity.GroupRegion;
import com.ohx.entity.Region;
import com.ohx.entity.ShipArchives;
import com.ohx.entity.TableAis;
import com.ohx.service.GroupRegionService;
import com.ohx.service.RegionService;
import com.ohx.service.ShipArchiveService;
import com.ohx.service.TableAisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class MyTest {

    @Autowired
    private ShipArchiveService service;

    @Autowired
    private GroupRegionService groupRegionService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private TableAisService tableAisService;




    @Test
    void testT(){
        List<ShipArchives> list = service.getShipByKeyword("ke");
        JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(list));
        System.out.println(array);
    }
    @Test
    void test6(){
        String mmsi = "777777777";
        QueryWrapper<ShipArchives> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mmsi", mmsi);
        List<ShipArchives> shipArchivesList =  service.list(queryWrapper);
        System.out.println(shipArchivesList);

        List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT type_name_cn, count(*) FROM ship_archives group by type_name_cn");

        JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(mapperList));




        System.out.println(list);
    }
    @Test
    void test1(){
        String column = "type_name_cn";
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
            } catch (Exception e) {
                e.printStackTrace();

            }



    }



    @Test
    void test2(){
        GroupRegion groupRegion = new GroupRegion();
        groupRegion.setGroupName("来了");
        groupRegion.setUserId("ohx00006");
        groupRegionService.save(groupRegion);
    }

    @Test
    void testRegion(){
        Region region = new Region();
        region.setRegionName("票p");
        region.setGroupId(2);
        region.setState(0);
        region.setMinLon("121");
        region.setMaxLon("1");
        region.setMinLat("1");
        region.setMaxLat("1");
        regionService.save(region);
    }

    @Test
    void testQuety(){
        QueryWrapper<GroupRegion> groupRegionQueryWrapper = new QueryWrapper<>();
        groupRegionQueryWrapper.eq("user_id", "ohx00006");
        List<GroupRegion> listGroupRegion = groupRegionService.list(groupRegionQueryWrapper);


        JSONArray jsonList = JSONArray.parseArray(JSONArray.toJSONString(listGroupRegion));

        System.out.println(jsonList.toJSONString());
    }

    @Test
    void testQuety1(){
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("group_id", 2);
        List<Region> listGroupRegion = regionService.list(regionQueryWrapper);

        JSONArray jsonList = JSONArray.parseArray(JSONArray.toJSONString(listGroupRegion));

        System.out.println(jsonList.toJSONString());
    }

    @Test
    void testQuety2(){
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("region_id", 2);

        Region object = regionService.getOne(regionQueryWrapper);

        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(object));

        System.out.println(jsonObject.toJSONString());
    }






    @Test
    void testQuety3(){

//        Integer regionId = 166;
//
//        QueryWrapper<TableAis> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("region_id", regionId);
//        List<TableAis> objectList = tableAisService.list(queryWrapper);
//
//        JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(objectList));
//
//        System.out.println(list.toJSONString());



        QueryWrapper<TableAis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("region_id", 0);
        List<TableAis> objectList = tableAisService.list(queryWrapper);

        JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(objectList));


        System.out.println(list.toJSONString());

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < objectList.size(); i++) {
            TableAis itemI = objectList.get(i);
            QueryWrapper<ShipArchives> queryWrappers = new QueryWrapper<>();
            queryWrappers.eq("mmsi", itemI.getMmsi());
            List<ShipArchives> shipArchivesList = service.list(queryWrappers);
            if(shipArchivesList != null && shipArchivesList.size() > 0){
                System.out.println("查询");
                JSONObject objectTableAis = JSONObject.parseObject(JSONObject.toJSONString(itemI));
                JSONObject objectShipArchives = JSONObject.parseObject(JSONObject.toJSONString(shipArchivesList.get(0)));
                objectTableAis.putAll(objectShipArchives);
                jsonArray.add(objectTableAis);
            }

        }

        System.out.println("长度：" + jsonArray.size());
        System.out.printf(jsonArray.toJSONString());
    }



    @Test
    void update1(){
        UpdateWrapper<GroupRegion> wrapper = new UpdateWrapper<>();
        wrapper.eq("group_id", 2);
        wrapper.set("group_name", "hello");

        groupRegionService.update(wrapper);
    }

    @Test
    void update11(){
        UpdateWrapper<Region> wrapper = new UpdateWrapper<>();
        wrapper.eq("region_id", 2);
        wrapper.set("region_name", "1");

        regionService.update(wrapper);
    }

    @Test
    void update112(){
        UpdateWrapper<Region> wrapper = new UpdateWrapper<>();
        wrapper.eq("region_id", 2);
        wrapper.set("state", 1);

        regionService.update(wrapper);
    }


    @Test
    void remove1(){
        QueryWrapper<GroupRegion> groupRegionQueryWrapper = new QueryWrapper<>();
        groupRegionQueryWrapper.eq("group_id", 3);

        groupRegionService.remove(groupRegionQueryWrapper);
    }

    @Test
    void testJoin1(){
        Integer groupid = 0;
        String start = "1718065059";
        String end   = "1718065391";
        try {
            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT  a.id, a.mmsi, a.region_id,a.ais_status, a.time, a.lon, a.lat, b.imo," +
                    "  b.call_sign, b.name_en, b.name_cn, b.flag, b.total_ton, b.load_ton, b.build_date, b.type_code, b.type_name_en, b.type_name_cn, b.status_code, b.status_string, b.length, b.wide, b.depth, b.freeboard, b.design_speed, b.design_draft, b.main_engine_speed\n" +
                    "FROM table_ais a LEFT  JOIN ship_archives b ON a.mmsi = b.mmsi  where a.region_id = " + groupid + " and time between " + start + " and" + " " + end);

            JSONArray list = JSONArray.parseArray(JSONArray.toJSONString(mapperList));

            System.out.println(list);




        } catch (Exception e) {
            e.printStackTrace();

        }



    }
}
