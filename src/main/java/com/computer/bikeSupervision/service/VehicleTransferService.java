package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.VehicleTransfer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.vo.findTransferVo;

import java.util.List;

/**
 * 车辆转让信息服务类
 */
public interface VehicleTransferService extends IService<VehicleTransfer> {

    /**
     * 新增二手转让信息
     */
    void addTransfer(VehicleTransfer vehicleTransfer, Long currentId);

    /**
     * 查询所有二手转让信息
     */
    List<VehicleTransfer> findAllTransfer(Long currentId);

    /**
     * 审核二手转让交易
     */
    void checkTransfer(VehicleTransfer vehicleTransfer, Long currentId);

    /**
     * 根据 id 查询二手转让信息
     */
    findTransferVo findTransfer(VehicleTransfer vehicleTransfer);
}
