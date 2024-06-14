package com.ohx.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohx.common.ResponseResult;
import com.ohx.entity.Neo4jTrack;
import com.ohx.entity.Track;
import com.ohx.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;

public interface UserService extends IService<User> {
    boolean checkEmaillRepeated(String email);

    boolean insertUser(User user);

//    ResponseResult getBoatListByPortName(String portName);

//    LinkedList<Track> getAISList(String boatName, String startTime, String endTime);
//
//    ArrayList<Neo4jTrack> getNeo4jList(String boatName, String startTime, String endTime);

//    ArrayList<Neo4jTrack> getNeo4jListMM(String mmsi, String startTime, String endTime);

}
