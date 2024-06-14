package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.GroupRegion;
import com.ohx.mapper.GroupRegionMapper;
import com.ohx.service.GroupRegionService;
import org.springframework.stereotype.Service;

@Service
public class GroupRegionServiceImpl extends ServiceImpl<GroupRegionMapper, GroupRegion> implements GroupRegionService {
}
