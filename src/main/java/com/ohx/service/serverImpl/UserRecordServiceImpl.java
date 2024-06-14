package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.UserRecord;
import com.ohx.mapper.UserRecordMapper;
import com.ohx.service.UserRecordService;
import org.springframework.stereotype.Service;

@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordService {
}
