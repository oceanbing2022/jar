package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.ApiRecord;
import com.ohx.mapper.ApiRecordMapper;
import com.ohx.service.ApiRecordService;
import org.springframework.stereotype.Service;

@Service
public class ApiRecordServiceImpl extends ServiceImpl<ApiRecordMapper, ApiRecord> implements ApiRecordService {
}
