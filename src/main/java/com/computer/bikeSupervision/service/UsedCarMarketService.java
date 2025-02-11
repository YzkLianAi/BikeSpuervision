package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 二手车交易信息服务表
 */
public interface UsedCarMarketService extends IService<UsedCarMarket> {

    /**
     * 发布二手车信息
     * @param usedCarMarketPublishDto
     * @param currentId
     */
    void addUsedCarMarket(UsedCarMarketPublishDto usedCarMarketPublishDto, Long currentId);

    /**
     * 查询二手车信息
     * @param currentId
     * @return
     */
    List<UsedCarMarket> queryUsedCarMarket(Long currentId);
}
