package com.computer.bikeSupervision.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 二手车市场发布
 */
@Data
public class UsedCarMarketPublishDto {
    /**
     * 联系方式
     */
    @TableField(value = "telephone")
    private String telephone;

    /**
     * 微信号
     */
    @TableField(value = "wechat_number")
    private String wechatNumber;

    /**
     * 车辆图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 车辆信息简述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 价格
     */
    @TableField(value = "price")
    private Double price;
}
