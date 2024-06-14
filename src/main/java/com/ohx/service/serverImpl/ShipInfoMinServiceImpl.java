
//模糊匹配
package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ohx.entity.ShipInfoMin;

import com.ohx.mapper.ShipInfoMinMapper;
import com.ohx.service.ShipInfoMinService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipInfoMinServiceImpl extends ServiceImpl<ShipInfoMinMapper, ShipInfoMin> implements ShipInfoMinService {

    public List<ShipInfoMin> getShipInfoMinByParameter(String parameter) {
        QueryWrapper<ShipInfoMin> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .likeRight("mmsi", parameter)  // 模糊匹配 mmsi
                .or()
                .likeRight("imo", parameter)   // 模糊匹配 imo
                .or()
                .like("name", parameter)       // 模糊匹配 name
        );
        return list(queryWrapper);
    }
}
