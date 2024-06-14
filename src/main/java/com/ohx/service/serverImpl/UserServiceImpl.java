package com.ohx.service.serverImpl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.common.BizCodeEnum;
import com.ohx.common.ResponseResult;
import com.ohx.dto.AreaRepository;
import com.ohx.dto.WarshipRepository;
import com.ohx.entity.Neo4jTrack;
import com.ohx.entity.Track;
import com.ohx.entity.User;
import com.ohx.entity.Warship;
import com.ohx.mapper.UserMapper;
import com.ohx.service.UserService;
import com.ohx.util.SetIdUtil;
import com.ohx.util.SortUtils;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;

import java.util.*;

import static com.ohx.common.Constant.USER_NICK_NAME_PREFIX;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserService userService;
    @Resource
    UserMapper userMapper;

    @Resource
    AreaRepository areaRepository;
    @Resource
    private Session session;

    @Resource
    WarshipRepository warshipRepository;

    @Resource
    private RestTemplate restTemplate;


    /**
     * 判断用户邮箱是否存在
     * @param email
     * @Author: 贾榕福
     * @Date: 2022/11/21
     */
    @Override
    public boolean checkEmaillRepeated(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        queryWrapper.last("limit 1");

        User user = userService.getOne(queryWrapper);
        //如果用户不存在
        if (user == null){
            return false;
        }
        return true;
    }

    /**
     * 插入用户
     * @param user
     * @Author: 贾榕福
     * @Date: 2022/11/21
     */
    @Override
    public boolean insertUser(User user) {
        int MaxId = userMapper.selectMaxId();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",MaxId)
                .last("limit 1");
        //原最新的数据
        User MaxUser = userService.getOne(queryWrapper);
        String userId = MaxUser.getUserId();
        user.setId(MaxId + 1);
        user.setUserId(SetIdUtil.setId(userId));
        user.setNickname(USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
        user.setType("普通用户");
        user.setPermission(2);
        
        return userService.saveOrUpdate(user);
    }

//    /**
//     *
//     * @param portName
//     * @Author: 贾榕福
//     * @Date: 2022/12/25
//     */
//    @Override
//    public ResponseResult getBoatListByPortName(String portName) {
//        //检测输入是否有效
//        if(portName==null || "".equals(portName)){
//            log.info("输入港口无效");
//            return ResponseResult.error(BizCodeEnum.PARAMETER_ERROR);
//        }
//        //检测输入的港口是否存在
//        if(areaRepository.findByname(portName)==null){
//            log.info("{}不存在:",portName);
//            return ResponseResult.error(BizCodeEnum.PORT_NOT);
//        }
//        //返回查询结果
//        log.info("进行港口关联船舶的查询:{}",portName);
//
//        String sql ="match (n:Area)-[r]-(m:Warship) where n.general_name=\"" + portName + "\" or n.name=~\".*" + portName + ".*\" return m,type(r) as act,r.time_start as time_start,r.time_end as time_end,r.for as for";
//        //数据库返回的查询结果
//        Result query = null;
//        ArrayList<Object> result = new ArrayList<>();
//        try{
//            query=session.query(sql,new HashMap<>());
//        }catch (Exception e){
//            log.info("20009  服务器繁忙");
//        }
//
//        //处理结果
//        if (query == null){
//            log.info("无数据！");
//        }else {
//            for(Map<String,Object> map : query.queryResults()){
//                //存船及时间
//                HashMap<String,Object> boat = new HashMap<>();
//                ArrayList<String> tmp;
//                //存时间
//                HashMap<String,Object> event = new HashMap<>();
//                Boolean flag = true;
//
//                Warship warship = (Warship) map.get("m");
//                Long time_start = (Long) map.get("time_start");
//                Long time_end = (Long) map.get("time_end");
//                String act = (String) map.get("act");
//                String forPort = (String) map.get("for");
//
//                tmp = warship.getLabels();
//                //军船项目中有，民船项目将其注释掉
////                if (!tmp.contains("Warship")){
////                    tmp.add("Warship");
////                    warship.setLabels(tmp);
////                }
//
//                event.put("time_start",time_start);
//                event.put("time_end",time_end);
//                event.put("act",act);
//                event.put("for",forPort);
//
//                for (int i = 0; i < result.size(); i++) {
//                    HashMap<String,Object> hashMap = (HashMap<String, Object>) result.get(i);
//                    if (hashMap.get("boat").equals(warship)){
//                        LinkedList<HashMap<String,Object>> linkList = (LinkedList<HashMap<String, Object>>) hashMap.get("linkList");
//                        linkList.add(event);
//                        flag = false;
//                    }
//                }
//                if (flag){
//                    boat.put("boat",warship);
//                    LinkedList<HashMap<String,Object>> linkList = new LinkedList<>();
//                    linkList.add(event);
//                    boat.put("linkList",linkList);
//                    result.add(boat);
//                }
//            }
//        }
//
//        log.info("查询港口关联船舶完毕！！！");
//        return ResponseResult.success(result);
//    }
//
//    /**
//     * 获取ais轨迹
//     *
//     * @param mmsi
//     * @param startTime
//     * @param endTime
//     * @Author: 贾榕福
//     * @Date: 2023/1/4
//     */
//    @Override
//    public LinkedList<Track> getAISList(String mmsi, String startTime, String endTime) {
//        //ais历史轨迹
//        LinkedList<Track> historyList = new LinkedList<>();
//
//        //所查询的船舶信息（mmsi，起止时间）
//        StringBuilder sb = new StringBuilder();
//        sb.append("{uid:\"userid\",mmsi:\"")
//                .append(mmsi)
//                .append("\",startdt:\"")
//                .append(startTime)
//                .append("\",enddt:\"")
//                .append(endTime)
//                .append("\"}")
//        ;
//        //转成字符串
//        String sMmsi = String.valueOf(sb);
//        byte[] bytes = sMmsi.getBytes();
//
//        BASE64Encoder encoder = new BASE64Encoder();
//        String param = encoder.encode(bytes).replaceAll("\r|\n", "");
//
//        //配置url所需要的变量
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("cmd","0x0151");
//        hashMap.put("param",param);
//        hashMap.put("time","1663907962");
//        hashMap.put("api_key","yAjFdLc$Mb76@9rC");
//
//
//        String result = restTemplate.getForObject("http://oceanstellar.gogotrade.info/api?cmd={cmd}&param={param}&time={time}&api_key={api_key}",
//                String.class,hashMap);
//
//        log.info("res:{}",result);
//
//        if ("[]".equals(result)){
//            return historyList;
//        }
//
//
//        List<String> list = JSONArray.parseArray(result,String.class);
//
//
//        for (String s : list){
//            String[] split = s.split("\\|");
//            Track boatHistory = new Track();
//            boatHistory.setFlag(Integer.valueOf(split[0]));
//            boatHistory.setLongitude(Float.valueOf(split[1]));
//            boatHistory.setLatitude(Float.valueOf(split[2]));
//            boatHistory.setTime(Long.valueOf(split[3]));
//            boatHistory.setDestination(split[4]);
//            boatHistory.setSpeed(split[5]);
//            boatHistory.setIgnore0(split[6]);
//            boatHistory.setCourse(split[7]);
//            boatHistory.setArriveTime(Long.valueOf(split[8]));
//            boatHistory.setIgnore1(split[9]);
//            boatHistory.setIgnore2(split[10]);
//            boatHistory.setEventId(Integer.valueOf(split[11]));
//            boatHistory.setStatus(split[12]);
//            boatHistory.setMmsi(mmsi);
//            historyList.add(boatHistory);
//        }
//
//        return historyList;
//    }
//
//    @Override
//    public ArrayList<Neo4jTrack> getNeo4jList(String boatName, String startTime, String endTime) {
//        String sql0 = "match (n:Warship)-[r]-(m:Area) where n.name=\"";
//        String sql2 = "\" and r.time_start>=";
//        String sql3 = " and r.time_start<=";
//        String sql4 = " return m.lon as lon,m.lat as lat,r.time_start as time_start,r.time_end as time_end,type(r) as act";
//        StringBuilder sb = new StringBuilder();
//        sb.append(sql0);
//        sb.append(boatName);
//        sb.append(sql2);
//        sb.append(startTime);
//        sb.append(sql3);
//        sb.append(endTime);
//        sb.append(sql4);
//        String cypherSql = sb.toString();
//        log.info("cypherSql:{}",cypherSql);
//
//        //数据库返回的查询结果
//        Result query = null;
//        //开源轨迹数据
//        ArrayList<Neo4jTrack> arrayList = new ArrayList<>();
//        try{
//            query=session.query(cypherSql,new HashMap<>());
//        }catch (Exception e){
//            log.info("20009  服务器繁忙");
//        }
//        if (query == null){
//            log.info("无数据！");
//        }else {
//            for(Map<String,Object> map : query.queryResults()){
//                Neo4jTrack neo4jTrack = new Neo4jTrack();
//                Double lon = (Double) map.get("lon");
//                Double lat = (Double) map.get("lat");
//                Long time_start = (Long) map.get("time_start");
//                Long time_end = (Long) map.get("time_end");
//                String act = (String) map.get("act");
//                if (lat == null || lat.equals("NULL") || lon == null || lon.equals("NULL")){
//                    continue;
//                }
//                neo4jTrack.setLon(lon);
//                neo4jTrack.setLat(lat);
//                neo4jTrack.setStartTime(time_start);
//                neo4jTrack.setEndTime(time_end);
//                neo4jTrack.setAct(act);
//                arrayList.add(neo4jTrack);
//            }
//        }
//
//        SortUtils.sort(arrayList,"startTime",true);
//        return arrayList;
//    }
//
//    @Override
//    public ArrayList<Neo4jTrack> getNeo4jListMM(String mmsi, String startTime, String endTime) {
//        String sql0 = "match (n:Warship)-[r]-(m:Area) where n.mmsi=\"";
//        String sql2 = "\" and r.time_start>=";
//        String sql3 = " and r.time_start<=";
//        String sql4 = " return m.lon as lon,m.lat as lat,r.time_start as time_start,r.time_end as time_end,type(r) as act";
//        StringBuilder sb = new StringBuilder();
//        sb.append(sql0);
//        sb.append(mmsi);
//        sb.append(sql2);
//        sb.append(startTime);
//        sb.append(sql3);
//        sb.append(endTime);
//        sb.append(sql4);
//        String cypherSql = sb.toString();
//        log.info("cypherSql:{}",cypherSql);
//
//        //数据库返回的查询结果
//        Result query = null;
//        //开源轨迹数据
//        ArrayList<Neo4jTrack> arrayList = new ArrayList<>();
//        try{
//            query=session.query(cypherSql,new HashMap<>());
//        }catch (Exception e){
//            log.info("20009  服务器繁忙");
//        }
//        if (query == null){
//            log.info("无数据！");
//        }else {
//            for(Map<String,Object> map : query.queryResults()){
//                Neo4jTrack neo4jTrack = new Neo4jTrack();
//                Double lon = (Double) map.get("lon");
//                Double lat = (Double) map.get("lat");
//                Long time_start = (Long) map.get("time_start");
//                Long time_end = (Long) map.get("time_end");
//                String act = (String) map.get("act");
//                if (lat == null || lat.equals("NULL") || lon == null || lon.equals("NULL")){
//                    continue;
//                }
//                neo4jTrack.setLon(lon);
//                neo4jTrack.setLat(lat);
//                neo4jTrack.setStartTime(time_start);
//                neo4jTrack.setEndTime(time_end);
//                neo4jTrack.setAct(act);
//                arrayList.add(neo4jTrack);
//            }
//        }
//
//        SortUtils.sort(arrayList,"startTime",true);
//        return arrayList;
//    }

}
