package com.computer.bikeSupervision.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 通行证添加
 */
@Data
public class PassReviewAddDto {
    /**
     * 车牌号
     */
    @TableField(value = "plate_number")
    private String plateNumber;

    /**
     * 车辆图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 证明材料
     */
    @TableField(value = "material")
    private String material;

    /**
     * 车辆品牌
     */
    @TableField(value = "vehicle_brand")
    private String vehicleBrand;

    /**
     * 电池类型
     */
    @TableField(value = "battery_type")
    private String batteryType;

    /**
     * 功率
     */
    @TableField(value = "power")
    private String power;

    /**
     * 电压
     */
    @TableField(value = "voltage")
    private String voltage;

    /**
     * 质量
     */
    @TableField(value = "quality")
    private String quality;

    /**
     * 续航
     */
    @TableField(value = "endurance")
    private String endurance;

    /**
     * 车牌类型
     */
    @TableField(value = "plate_type")
    private String plateType;

    /**
     * 预估价值
     */
    @TableField(value = "price")
    private String price;

}
