package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.baomidou.mybatisplus.extension.service.IService;


public interface PlatePassService extends IService<PlatePass> {

    /**
     * 生成二维码
     * @param id
     * @return
     */
    String generateSqCode(Long id) throws Exception;
}
