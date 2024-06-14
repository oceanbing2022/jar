package com.ohx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ohx.entity.ShipArchives;

import java.util.List;

public interface ShipArchiveService extends IService<ShipArchives> {
    List<ShipArchives> getShipByKeyword(String keyword);
}
