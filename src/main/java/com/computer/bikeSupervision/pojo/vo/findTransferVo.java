package com.computer.bikeSupervision.pojo.vo;

import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import lombok.Data;

/*
  查询单个二手转让详细信息
 */
@Data
public class findTransferVo {

    private UsedCarMarket usedCarMarket;

    private Integer sellerVehicleCount;

    private boolean sellerHasViolation;

    private Integer buyerVehicleCount;

    private boolean buyerHasViolation;
}

