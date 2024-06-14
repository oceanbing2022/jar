package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.ShipArchives;
import com.ohx.mapper.ShipArchiveMapper;
import com.ohx.service.ShipArchiveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipArchiveServiceImpl extends ServiceImpl<ShipArchiveMapper, ShipArchives> implements ShipArchiveService {

    @Override
    public List<ShipArchives> getShipByKeyword(String keyword) {
        QueryWrapper<ShipArchives> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .like("mmsi", keyword)  // 模糊匹配 mmsi
                .or()
                .like("imo", keyword)   // 模糊匹配 imo
                .or()
                .like("name_en", keyword)       // 模糊匹配 name
                .or()
                .like("name_cn", keyword)
                .or()
                .like("call_sign", keyword)
        );

        queryWrapper.last("LIMIT 100");
        return list(queryWrapper);
    }
}
