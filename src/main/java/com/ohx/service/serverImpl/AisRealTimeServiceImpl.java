package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.AisRealTime;
import com.ohx.mapper.AisRealTimeMapper;
import com.ohx.service.AisRealTimeService;
import org.springframework.stereotype.Service;

@Service
public class AisRealTimeServiceImpl extends ServiceImpl<AisRealTimeMapper, AisRealTime>  implements AisRealTimeService {
}
