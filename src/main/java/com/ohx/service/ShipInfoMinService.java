package com.ohx.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.ohx.entity.ShipInfoMin;

import java.util.List;

public interface ShipInfoMinService extends IService<ShipInfoMin> {
    List<ShipInfoMin> getShipInfoMinByParameter(String parameter);
}
