//
////模糊匹配
//package com.ohx.service.serverImpl;
//
//        import com.alibaba.fastjson2.JSON;
//        import com.alibaba.fastjson2.JSONObject;
//        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//        import com.ohx.common.ResponseResult;
//        import com.ohx.entity.ShipInfo;
//        import com.ohx.entity.ShipInfoMin;
//        import com.ohx.mapper.ShipInfoMapper;
//        import com.ohx.service.ShipInfoService;
//        import org.apache.ibatis.annotations.Param;
//        import org.springframework.stereotype.Service;
//
//        import java.util.Base64;
//        import java.util.List;
//
//        import static com.ohx.common.BizCodeEnum.PARAMETER_ERROR;
//
//@Service
//public class ShipInfoServiceImpl extends ServiceImpl<ShipInfoMapper, ShipInfo> implements ShipInfoService {
//
//    public List<ShipInfo> getShipInfoByParameter(String parameter) {
//        QueryWrapper<ShipInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.and(wrapper -> wrapper
//                .likeRight("mmsi", parameter)  // 模糊匹配 mmsi
//                .or()
//                .likeRight("imo", parameter)   // 模糊匹配 imo
//                .or()
//                .like("name", parameter)       // 模糊匹配 name
//        );
//        return list(queryWrapper);
//    }
//}
