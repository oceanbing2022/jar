package com.ohx.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.PassToken;
import com.ohx.common.ResponseResult;
import com.ohx.common.UserLoginToken;
import com.ohx.dto.WarshipRepository;
import com.ohx.entity.AisRealTime;
import com.ohx.entity.Warship;
import com.ohx.service.AisRealTimeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hsyq/service")
@Slf4j

public class AisRealTimeController {

    @Autowired
    private WarshipRepository warshipRepository;

    @UserLoginToken
//    @PassToken
    @GetMapping("/getRegionalAis")
    public ResponseResult getRegionalAis(@Param("param")String param){

        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);
        JSONArray jsonArrayLB = param_json.getJSONArray("lb");
        JSONArray jsonArrayRT = param_json.getJSONArray("rt");
        String lb = (String) jsonArrayLB.get(0) + "," + (String) jsonArrayLB.get(1);
        String rt = (String) jsonArrayRT.get(0) + "," + (String) jsonArrayRT.get(1);

        String p="{lb:\""+ lb + "\",rt:\"" + rt +  "\"}";
        log.info(p);
        //将参数进行base64编码
        Base64.Encoder encoder=Base64.getEncoder();
        String P=encoder.encodeToString(p.getBytes());
        //构造请求URL
        String url="http://oceanstellar.gogotrade.info/api?cmd=0x5113&param="+P;
        //进行HTTP请求查询
        JSONObject result;
        try {
            String response = getRequestRegionalAis(url);
            if(response == ""){
                return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
            }
            return ResponseResult.success(JSON.parseObject(getRequestRegionalAis(url)));
        }catch (Exception e){
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }


    @UserLoginToken
    @GetMapping("/getRegionalAisWithAnalysis")
    public ResponseResult getRegionalAisWithAnalysis(@Param("param")String param){

//        Warship[] warShips = warshipRepository.findAllWarshipList();
//        Warship[] coastGuardShips = warshipRepository.findAllCoastGuardList();


        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        double lon = param_json.getDouble("lon");
        double lat = param_json.getDouble("lat");
        boolean bigFlag = param_json.getBoolean("isBig");


        double[] loc = new double[]{lon, lat};

        System.out.println("查询大港吗？ " + bigFlag);

        if(!bigFlag){
            String lb = (loc[0] - 0.25) + "," + (loc[1] -0.25);
            String rt = (loc[0] + 0.25) + "," + (loc[1] + 0.25);
            String p="{lb:\""+ lb + "\",rt:\"" + rt +  "\"}";
            log.info(p);
            //将参数进行base64编码
            Base64.Encoder encoder=Base64.getEncoder();
            String P=encoder.encodeToString(p.getBytes());
            //构造请求URL
            String url="http://oceanstellar.gogotrade.info/api?cmd=0x5113&param="+P;
            //进行HTTP请求查询
            JSONObject result;
            try {
                String response = getRequestRegionalAis(url);
                if(response == ""){
                    return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
                }

                JSONArray list = JSON.parseObject(response).getJSONArray("ships");


//            JSONArray warship
                JSONArray warshipList = new JSONArray();
                JSONArray notWarshipList = new JSONArray();

                for (int i = 0; i < list.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) list.get(i);
//                    for (int j = 0; j < warShips.length; j++) {
//                        if ((!(item.getString("mmsi").equals("0")) && item.getString("mmsi").equals(warShips[j].getMmsi())) || (!(item.getString("imo").equals("0")) && item.getString("imo").equals(warShips[j].getImo()))) {
//                            flag = true;
//                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + warShips[j].getMmsi());
//                            break;
//                        }
//                    }
//                    if (flag) {
//                        warshipList.add(item);
//                    } else {
//                        notWarshipList.add(item);
//                    }
                    notWarshipList.add(item);
                }

                System.out.println("一次：" + notWarshipList.size());

                JSONArray coastGuardList = new JSONArray();
                JSONArray notCoastGuardList = new JSONArray();

                for (int i = 0; i < notWarshipList.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) notWarshipList.get(i);
//                    for (int j = 0; j < coastGuardShips.length; j++) {
////					System.out.println(item.getString("mmsi") + "---------" + coastGuardShips[j].getMmsi());
//                        if ((!item.getString("mmsi").equals("0") && item.getString("mmsi").equals(coastGuardShips[j].getMmsi())) || (!(item.getString("imo").equals("0")) &&   item.getString("imo").equals(coastGuardShips[j].getImo()))) {
//                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + coastGuardShips[j].getMmsi());
//                            System.out.println("imo : " + item.getString("imo") + "---" + coastGuardShips[j].getImo());
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag) {
//                        coastGuardList.add(item);
//                    } else {
//                        notCoastGuardList.add(item);
//                    }
                    notCoastGuardList.add(item);
                }

                System.out.println("总数：" + list.size());
                System.out.println("军舰：" + warshipList.size());
                System.out.println("军警：" + coastGuardList.size());
                System.out.println("民船：" + notCoastGuardList.size());

                JSONArray workShipList = new JSONArray();
                JSONArray specialShipList = new JSONArray();
                JSONArray customShipList = new JSONArray();
                JSONArray goodsShipList = new JSONArray();
                JSONArray oilShipList  = new JSONArray();
                JSONArray otherShipList = new JSONArray();

                for (int i = 0; i < notCoastGuardList.size(); i++) {
                    JSONObject item = (JSONObject) notCoastGuardList.get(i);
                    String shipType = item.getString("ship_type") + "";
                    if(shipType.length() == 2){
                        int flag = Integer.parseInt(shipType.charAt(0) + "");
                        switch (flag){
                            case 3:
                                workShipList.add(item);
                                break;
                            case 5:
                                specialShipList.add(item);
                                break;
                            case 6:
                                customShipList.add(item);
                                break;
                            case 7:
                                goodsShipList.add(item);
                                break;
                            case 8:
                                oilShipList.add(item);
                                break;
                            default:
                                otherShipList.add(item);
                                break;
                        }
                    }else {
                        otherShipList.add(item);
                    }
                }

                System.out.println("workShipList: " + workShipList.size());
                System.out.println("specialShipList: " + specialShipList.size());
                System.out.println("customShipList: " + customShipList.size());
                System.out.println("goodsShipList: " + goodsShipList.size());
                System.out.println("oilShipList: " + oilShipList.size());
                System.out.println("otherShipList: " + otherShipList.size());



                JSONObject chartData = new JSONObject();

                JSONObject number = new JSONObject();
                number.put("total", list.size());
//                number.put("warshipTotal", warshipList.size());
//                number.put("coastGuardTotal", coastGuardList.size());
                number.put("normalShipTotal", notCoastGuardList.size());
                number.put("workShipTotal", workShipList.size());
                number.put("specialShipTotal", specialShipList.size());
                number.put("customShipTotal", customShipList.size());
                number.put("goodsShipTotal", goodsShipList.size());
                number.put("oilShipTotal", oilShipList.size());
                number.put("otherShipTotal", otherShipList.size());

                JSONObject warshipListForJson = new JSONObject();
//                warshipListForJson.put("warshipList", warshipList);
//                warshipListForJson.put("coastGuardList", coastGuardList);
                warshipListForJson.put("workShipList", workShipList);
                warshipListForJson.put("specialShipList", specialShipList);
                warshipListForJson.put("customShipList", customShipList);
                warshipListForJson.put("goodsShipList", goodsShipList);
                warshipListForJson.put("oilShipList", oilShipList);
                warshipListForJson.put("otherShipList", otherShipList);


                chartData.put("number", number);
                chartData.put("warshipList", warshipListForJson);

//                System.out.println(chartData);

                return ResponseResult.success(chartData);
            }catch (Exception e){
                log.info("20009  服务器繁忙");
                return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
            }
        } else {

            //进行HTTP请求查询
            JSONObject result;
            try {

                JSONArray list = new JSONArray();

                double[] loc1 = new double[] {loc[0]-0.2, loc[1]+0.2};
                double[] loc2 = new double[] {loc[0]-0.2, loc[1]-0.2};
                double[] loc3 = new double[] {loc[0]+0.2, loc[1]+0.2};
                double[] loc4 = new double[] {loc[0]+0.2, loc[1]-0.2};

                list.addAll(requestAllInList(loc1, 0.2));
                list.addAll(requestAllInList(loc2, 0.2));
                list.addAll(requestAllInList(loc3, 0.2));
                list.addAll(requestAllInList(loc4, 0.2));

//            JSONArray warship
                JSONArray warshipList = new JSONArray();
                JSONArray notWarshipList = new JSONArray();

                System.out.println("总数量" + list.size());




                for (int i = 0; i < list.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) list.get(i);
//                    for (int j = 0; j < warShips.length; j++) {
//                        if (((item.getString("mmsi").length() > 6) && item.getString("mmsi").equals(warShips[j].getMmsi())) || ((item.getString("imo").length() > 6) && item.getString("imo").equals(warShips[j].getImo()))) {
//                            flag = true;
//                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + warShips[j].getMmsi());
//                            break;
//                        }
//                    }
//                    if (flag) {
//                        warshipList.add(item);
//                    } else {
//                        notWarshipList.add(item);
//                    }
                    notWarshipList.add(item);
                }

                System.out.println("一次：" + notWarshipList.size());

                JSONArray coastGuardList = new JSONArray();
                JSONArray notCoastGuardList = new JSONArray();

                for (int i = 0; i < notWarshipList.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) notWarshipList.get(i);
//                    for (int j = 0; j < coastGuardShips.length; j++) {
////					System.out.println(item.getString("mmsi") + "---------" + coastGuardShips[j].getMmsi());
//                        if ((!item.getString("mmsi").equals("0") && item.getString("mmsi").equals(coastGuardShips[j].getMmsi())) || (!(item.getString("imo").equals("0")) &&   item.getString("imo").equals(coastGuardShips[j].getImo()))) {
//                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + coastGuardShips[j].getMmsi());
//                            System.out.println("imo : " + item.getString("imo") + "---" + coastGuardShips[j].getImo());
//                            flag = true;
//                            break;
//                        }
//                    }
//                    if (flag) {
//                        coastGuardList.add(item);
//                    } else {
//                        notCoastGuardList.add(item);
//                    }
                    notCoastGuardList.add(item);
                }

                System.out.println("总数：" + list.size());
                System.out.println("军舰：" + warshipList.size());
                System.out.println("军警：" + coastGuardList.size());
                System.out.println("民船：" + notCoastGuardList.size());

                JSONArray workShipList = new JSONArray();
                JSONArray specialShipList = new JSONArray();
                JSONArray customShipList = new JSONArray();
                JSONArray goodsShipList = new JSONArray();
                JSONArray oilShipList  = new JSONArray();
                JSONArray otherShipList = new JSONArray();

                for (int i = 0; i < notCoastGuardList.size(); i++) {
                    JSONObject item = (JSONObject) notCoastGuardList.get(i);
                    String shipType = item.getString("ship_type") + "";
                    if(shipType.length() == 2){
                        int flag = Integer.parseInt(shipType.charAt(0) + "");
                        switch (flag){
                            case 3:
                                workShipList.add(item);
                                break;
                            case 5:
                                specialShipList.add(item);
                                break;
                            case 6:
                                customShipList.add(item);
                                break;
                            case 7:
                                goodsShipList.add(item);
                                break;
                            case 8:
                                oilShipList.add(item);
                                break;
                            default:
                                otherShipList.add(item);
                                break;
                        }
                    }else {
                        otherShipList.add(item);
                    }
                }

                System.out.println("workShipList: " + workShipList.size());
                System.out.println("specialShipList: " + specialShipList.size());
                System.out.println("customShipList: " + customShipList.size());
                System.out.println("goodsShipList: " + goodsShipList.size());
                System.out.println("oilShipList: " + oilShipList.size());
                System.out.println("otherShipList: " + otherShipList.size());



                JSONObject chartData = new JSONObject();

                JSONObject number = new JSONObject();
                number.put("total", list.size());
//                number.put("warshipTotal", warshipList.size());
//                number.put("coastGuardTotal", coastGuardList.size());
                number.put("normalShipTotal", notCoastGuardList.size());
                number.put("workShipTotal", workShipList.size());
                number.put("specialShipTotal", specialShipList.size());
                number.put("customShipTotal", customShipList.size());
                number.put("goodsShipTotal", goodsShipList.size());
                number.put("oilShipTotal", oilShipList.size());
                number.put("otherShipTotal", otherShipList.size());

                JSONObject warshipListForJson = new JSONObject();
//                warshipListForJson.put("warshipList", warshipList);
//                warshipListForJson.put("coastGuardList", coastGuardList);
                warshipListForJson.put("workShipList", workShipList);
                warshipListForJson.put("specialShipList", specialShipList);
                warshipListForJson.put("customShipList", customShipList);
                warshipListForJson.put("goodsShipList", goodsShipList);
                warshipListForJson.put("oilShipList", oilShipList);
                warshipListForJson.put("otherShipList", otherShipList);


                chartData.put("number", number);
                chartData.put("warshipList", warshipListForJson);

//            System.out.println(chartData);

                return ResponseResult.success(chartData);
            }catch (Exception e){
                log.info("20009  服务器繁忙");
                return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
            }
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
    
    private JSONArray requestAllInList(double[] loc, double num){
        String lb1 = (loc[0] - num) + "," + (loc[1]);
        String rt1 = (loc[0]) + "," + (loc[1] + num);
        String lb2 = (loc[0]) + "," + (loc[1]);
        String rt2 = (loc[0] + num) + "," + (loc[1] + num);
        String lb3 = (loc[0] - num) + "," + (loc[1] - num);
        String rt3 = (loc[0]) + "," + (loc[1]);
        String lb4 = (loc[0]) + "," + (loc[1] - num);
        String rt4 = (loc[0] + num) + "," + (loc[1]);
        String p1 = "{lb:\"" + lb1 + "\",rt:\"" + rt1 + "\"}";
        String p2 = "{lb:\"" + lb2 + "\",rt:\"" + rt2 + "\"}";
        String p3 = "{lb:\"" + lb3 + "\",rt:\"" + rt3 + "\"}";
        String p4 = "{lb:\"" + lb4 + "\",rt:\"" + rt4 + "\"}";
//		System.out.println(p);
        //将参数进行base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        String P1 = encoder.encodeToString(p1.getBytes());
        String P2 = encoder.encodeToString(p2.getBytes());
        String P3 = encoder.encodeToString(p3.getBytes());
        String P4 = encoder.encodeToString(p4.getBytes());
        //构造请求URL
        String url1 = "http://oceanstellar.gogotrade.info/api?cmd=0x5113&param=" + P1;
        String url2 = "http://oceanstellar.gogotrade.info/api?cmd=0x5113&param=" + P2;
        String url3 = "http://oceanstellar.gogotrade.info/api?cmd=0x5113&param=" + P3;
        String url4 = "http://oceanstellar.gogotrade.info/api?cmd=0x5113&param=" + P4;
        //进行HTTP请求查询
        JSONObject result;
        try {
            String response1 = getRequestRegionalAis(url1);
            System.out.println("response1: 原始数据：" + JSON.parseObject(response1).getInteger("count") + "-" + JSON.parseObject(response1).getJSONArray("ships").size());

            JSONArray list1 = JSON.parseObject(response1).getJSONArray("ships");

            String response2 = getRequestRegionalAis(url2);
            System.out.println("response2 " + JSON.parseObject(response2).getInteger("count") + "-" + JSON.parseObject(response2).getJSONArray("ships").size());

            JSONArray list2 = JSON.parseObject(response2).getJSONArray("ships");

            String response3 = getRequestRegionalAis(url3);
            System.out.println("response3 " + JSON.parseObject(response3).getInteger("count") + "-" + JSON.parseObject(response3).getJSONArray("ships").size());

            JSONArray list3 = JSON.parseObject(response3).getJSONArray("ships");

            String response4 = getRequestRegionalAis(url4);
            System.out.println("response4 " + JSON.parseObject(response4).getInteger("count") + "-" + JSON.parseObject(response4).getJSONArray("ships").size());

            JSONArray list4 = JSON.parseObject(response4).getJSONArray("ships");


            JSONArray list = new JSONArray();
            list.addAll(list1);
            list.addAll(list2);
            list.addAll(list3);
            list.addAll(list4);
            return list;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    @Autowired
    private AisRealTimeService aisRealTimeService;

    @UserLoginToken
    @GetMapping("/getAisData")
    public ResponseResult getAisData(@Param("param")String param){

//        Base64.Decoder decoder=Base64.getDecoder();
//        String param_decode=new String(decoder.decode(param));
//        log.info("param_decode:{}",param_decode);
//        JSONObject param_json= JSON.parseObject(param_decode);





        JSONObject result;
        try {

              List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT mmsi,lon,lat FROM `ais_real_time`");

              log.info("mapperList 大小： " + mapperList.size());

              JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(mapperList));

              log.info("array 大小： " + array.size());

            return ResponseResult.success(array);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }


//    @UserLoginToken
    @PassToken
    @GetMapping("/getAisDataByBound")
    public ResponseResult getAisDataByBound(@Param("param")String param){
        try {
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(param));
            log.info("param_decode:{}",param_decode);
            JSONObject param_json= JSON.parseObject(param_decode);

            double lonSmall = param_json.getDouble("lonSmall");
            double lonBig = param_json.getDouble("lonBig");
            double latSmall = param_json.getDouble("latSmall");
            double latBig = param_json.getDouble("latBig");

            QueryWrapper<AisRealTime> aisRealTimeQueryWrapper = new QueryWrapper<>();
            aisRealTimeQueryWrapper.between("lon", lonSmall, lonBig);
            aisRealTimeQueryWrapper.between("lat", latSmall, latBig);
            List<AisRealTime> list = aisRealTimeService.list(aisRealTimeQueryWrapper);

            for (int i = 0; i < list.size(); i++) {
                list.get(i).setTimeStamp(list.get(i).getTimeStamp() + 8 * 3600);
            }

            JSONArray array= JSONArray.parseArray(JSON.toJSONString(list));

            return ResponseResult.success(array);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }

//    @UserLoginToken
    @PassToken
    @GetMapping("/getAisDataByBoundSmall")
    public ResponseResult  getAisDataByBoundSmall(@Param("param")String param){
        try {
            Base64.Decoder decoder=Base64.getDecoder();
            String param_decode=new String(decoder.decode(param));
            log.info("param_decode:{}",param_decode);
            JSONObject param_json= JSON.parseObject(param_decode);

            double lonSmall = param_json.getDouble("lonSmall");
            double lonBig = param_json.getDouble("lonBig");
            double latSmall = param_json.getDouble("latSmall");
            double latBig = param_json.getDouble("latBig");

            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT mmsi,lon,lat FROM `ais_real_time` where lon > {0} and lon < {1} and lat > {2} and lat < {3}", lonSmall, lonBig, latSmall, latBig );

            log.info("mapperList 大小： " + mapperList.size());

            JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(mapperList));

            return ResponseResult.success(array);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }


    @UserLoginToken
    @GetMapping("/getAisDataLimit")
    public ResponseResult getAisDataLimit(@Param("param")String param){

//        Base64.Decoder decoder=Base64.getDecoder();
//        String param_decode=new String(decoder.decode(param));
//        log.info("param_decode:{}",param_decode);
//        JSONObject param_json= JSON.parseObject(param_decode);





        JSONObject result;
        try {

            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT mmsi,lon,lat FROM `ais_real_time` ORDER BY RAND() LIMIT 20000");

            log.info("mapperList 大小： " + mapperList.size());

            JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(mapperList));

            log.info("array 大小： " + array.size());

            return ResponseResult.success(array);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }


    @PassToken
    @GetMapping("/getRandomAis")
    public ResponseResult getRandomAis(@Param("param")String param){

        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);

        Integer num = param_json.getInteger("num");



        JSONObject result;
        try {
            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT * FROM ais_real_time order by rand() limit {0}", num);

            log.info("mapperList 大小： " + mapperList.size());

            JSONArray array = JSONArray.parseArray(JSONObject.toJSONString(mapperList));

            log.info("array 大小： " + array.size());

            return ResponseResult.success(array);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }

    @PassToken
    @GetMapping("/getBMString")
    public String getBMString(@Param("param")String param){

        log.info(param);
        return getRequestRegionalAis("http://oceanstellar.gogotrade.info/api?" + param);

    }

    @PassToken
    @GetMapping("/getTotalAisNum")
    public ResponseResult getTotalAisNum(@Param("param")String param){

        Integer num;
        try {
            List<Map<String, Object>> mapperList =  SqlRunner.db().selectList("SELECT count(*) as num FROM ais_real_time");

            num = Integer.parseInt(mapperList.get(0).get("num").toString()) ;

            log.info("总数大小： " + num);

            return ResponseResult.success(num);
        }catch (Exception e){
            e.printStackTrace();
            log.info("20009  服务器繁忙");
            return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
        }
    }

    @UserLoginToken
    @GetMapping("/getAisShipByBound")
    public ResponseResult getAisShipByBound(@Param("param")String param){

        Warship[] warShips = warshipRepository.findAllWarshipList();
        Warship[] coastGuardShips = warshipRepository.findAllCoastGuardList();

        Base64.Decoder decoder=Base64.getDecoder();
        String param_decode=new String(decoder.decode(param));
        log.info("param_decode:{}",param_decode);
        JSONObject param_json= JSON.parseObject(param_decode);
        JSONArray jsonArrayLB = param_json.getJSONArray("lb");
        JSONArray jsonArrayRT = param_json.getJSONArray("rt");
        String lb = (String) jsonArrayLB.get(0) + "," + (String) jsonArrayLB.get(1);
        String rt = (String) jsonArrayRT.get(0) + "," + (String) jsonArrayRT.get(1);





            String p="{lb:\""+ lb + "\",rt:\"" + rt +  "\"}";
            log.info(p);
            //将参数进行base64编码
            Base64.Encoder encoder=Base64.getEncoder();
            String P=encoder.encodeToString(p.getBytes());
            //构造请求URL
            String url="http://oceanstellar.gogotrade.info/api?cmd=0x5113&param="+P;
            //进行HTTP请求查询
            JSONObject result;
            try {
                String response = getRequestRegionalAis(url);
                if(response == ""){
                    return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
                }

                log.info("+++++++++++++++++++++++++++++++++++" + response);

                JSONArray list = JSON.parseObject(response).getJSONArray("ships");



//            JSONArray warship
                JSONArray warshipList = new JSONArray();
                JSONArray notWarshipList = new JSONArray();

                for (int i = 0; i < list.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) list.get(i);
                    for (int j = 0; j < warShips.length; j++) {
                        if ((!(item.getString("mmsi").equals("0")) && item.getString("mmsi").equals(warShips[j].getMmsi())) || (!(item.getString("imo").equals("0")) && item.getString("imo").equals(warShips[j].getImo()))) {
                            flag = true;
                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + warShips[j].getMmsi());
                            break;
                        }
                    }
                    if (flag) {
                        warshipList.add(item);
                    } else {
                        notWarshipList.add(item);
                    }
                }

                System.out.println("一次：" + notWarshipList.size());

                JSONArray coastGuardList = new JSONArray();
                JSONArray notCoastGuardList = new JSONArray();

                for (int i = 0; i < notWarshipList.size(); i++) {
                    boolean flag = false;
                    JSONObject item = (JSONObject) notWarshipList.get(i);
                    for (int j = 0; j < coastGuardShips.length; j++) {
//					System.out.println(item.getString("mmsi") + "---------" + coastGuardShips[j].getMmsi());
                        if ((!item.getString("mmsi").equals("0") && item.getString("mmsi").equals(coastGuardShips[j].getMmsi())) || (!(item.getString("imo").equals("0")) &&   item.getString("imo").equals(coastGuardShips[j].getImo()))) {
                            System.out.println("mmsi : " + item.getString("mmsi") + "---" + coastGuardShips[j].getMmsi());
                            System.out.println("imo : " + item.getString("imo") + "---" + coastGuardShips[j].getImo());
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        coastGuardList.add(item);
                    } else {
                        notCoastGuardList.add(item);
                    }
                }

                System.out.println("总数：" + list.size());
                System.out.println("军舰：" + warshipList.size());
                System.out.println("军警：" + coastGuardList.size());
                System.out.println("民船：" + notCoastGuardList.size());

                JSONArray workShipList = new JSONArray();
                JSONArray specialShipList = new JSONArray();
                JSONArray customShipList = new JSONArray();
                JSONArray goodsShipList = new JSONArray();
                JSONArray oilShipList  = new JSONArray();
                JSONArray otherShipList = new JSONArray();

                for (int i = 0; i < notCoastGuardList.size(); i++) {
                    JSONObject item = (JSONObject) notCoastGuardList.get(i);
                    String shipType = item.getString("ship_type") + "";
                    if(shipType.length() == 2){
                        int flag = Integer.parseInt(shipType.charAt(0) + "");
                        switch (flag){
                            case 3:
                                workShipList.add(item);
                                break;
                            case 5:
                                specialShipList.add(item);
                                break;
                            case 6:
                                customShipList.add(item);
                                break;
                            case 7:
                                goodsShipList.add(item);
                                break;
                            case 8:
                                oilShipList.add(item);
                                break;
                            default:
                                otherShipList.add(item);
                                break;
                        }
                    }else {
                        otherShipList.add(item);
                    }
                }

                System.out.println("workShipList: " + workShipList.size());
                System.out.println("specialShipList: " + specialShipList.size());
                System.out.println("customShipList: " + customShipList.size());
                System.out.println("goodsShipList: " + goodsShipList.size());
                System.out.println("oilShipList: " + oilShipList.size());
                System.out.println("otherShipList: " + otherShipList.size());



                JSONObject chartData = new JSONObject();

                JSONObject number = new JSONObject();
                number.put("total", list.size());
                number.put("warshipTotal", warshipList.size());
                number.put("coastGuardTotal", coastGuardList.size());
                number.put("normalShipTotal", notCoastGuardList.size());
                number.put("workShipTotal", workShipList.size());
                number.put("specialShipTotal", specialShipList.size());
                number.put("customShipTotal", customShipList.size());
                number.put("goodsShipTotal", goodsShipList.size());
                number.put("oilShipTotal", oilShipList.size());
                number.put("otherShipTotal", otherShipList.size());

                JSONObject warshipListForJson = new JSONObject();
                warshipListForJson.put("warshipList", warshipList);
                warshipListForJson.put("coastGuardList", coastGuardList);
                warshipListForJson.put("workShipList", workShipList);
                warshipListForJson.put("specialShipList", specialShipList);
                warshipListForJson.put("customShipList", customShipList);
                warshipListForJson.put("goodsShipList", goodsShipList);
                warshipListForJson.put("oilShipList", oilShipList);
                warshipListForJson.put("otherShipList", otherShipList);


                chartData.put("number", number);
                chartData.put("warshipList", warshipListForJson);

//                System.out.println(chartData);

                JSONArray resultLists = new JSONArray();

                for(int i=0; i<warshipList.size(); i++){
                    JSONObject item =  (JSONObject) warshipList.get(i);
                    item.put("selfNum", 0);
                    resultLists.add(item);
                }

                for(int i=0; i<coastGuardList.size(); i++){
                    JSONObject item =  (JSONObject) coastGuardList.get(i);
                    item.put("selfNum", 1);
                    resultLists.add(item);
                }

                for(int i=0; i<workShipList.size(); i++){
                    JSONObject item =  (JSONObject) workShipList.get(i);
                    item.put("selfNum", 2);
                    resultLists.add(item);
                }

                for(int i=0; i<specialShipList.size(); i++){
                    JSONObject item =  (JSONObject) specialShipList.get(i);
                    item.put("selfNum", 3);
                    resultLists.add(item);
                }

                for(int i=0; i<customShipList.size(); i++){
                    JSONObject item =  (JSONObject) customShipList.get(i);
                    item.put("selfNum", 4);
                    resultLists.add(item);
                }

                for(int i=0; i<goodsShipList.size(); i++){
                    JSONObject item =  (JSONObject) goodsShipList.get(i);
                    item.put("selfNum", 5);
                    resultLists.add(item);
                }

                for(int i=0; i<oilShipList.size(); i++){
                    JSONObject item =  (JSONObject) oilShipList.get(i);
                    item.put("selfNum", 6);
                    resultLists.add(item);
                }

                for(int i=0; i<otherShipList.size(); i++){
                    JSONObject item =  (JSONObject) otherShipList.get(i);
                    item.put("selfNum", 7);
                    resultLists.add(item);
                }

                return ResponseResult.success(resultLists);
            }catch (Exception e){
                log.info("20009  服务器繁忙");
                e.printStackTrace();
                return ResponseResult.error(BizCodeEnum.BUSY_SERVICE);
            }

    }

}
