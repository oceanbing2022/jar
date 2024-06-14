package com.ohx.service.serverImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohx.entity.TableAis;
import com.ohx.mapper.TableAisMapper;
import com.ohx.service.TableAisService;
import org.springframework.stereotype.Service;

@Service
public class TableAisServiceImpl extends ServiceImpl<TableAisMapper, TableAis> implements TableAisService {
}
