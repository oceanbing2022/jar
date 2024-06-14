//package com.ohx.service.serverImpl;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.ohx.entity.Location;
//import com.ohx.mapper.LocationMapper;
//import com.ohx.service.LocationService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {
//}


package com.ohx.service.serverImpl;

        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import com.ohx.entity.Location;
        import com.ohx.mapper.LocationMapper;
        import com.ohx.service.LocationService;
        import org.springframework.stereotype.Service;

        import java.util.List;

@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements LocationService {

    @Override
    public List<Location> getLocationByKeyword(String keyword) {
        QueryWrapper<Location> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper -> wrapper
                .like("name", keyword)       // 模糊匹配 name
                .or()
                .like("cn_name", keyword)    // 模糊匹配 cnName
        );
        return list(queryWrapper);
    }
}

