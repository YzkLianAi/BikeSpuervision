package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

/**
 * 单条违章信息
 */
@Data
public class ViolationOneVo {
    /**
     * 主键id
     */
    private String id;

    /**
     * 车牌号
     */
    private String licencePlate;

    /**
     * 违法图片
     */
    private String image;

    /**
     * 违法原因
     */
    private String cause;

    /**
     * 车辆类型
     */
    private String vehicleType;

}
