package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 二手车交易信息发布表
 */
@TableName(value ="used_car_market")
@Data
public class UsedCarMarket implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 学号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 学校
     */
    @TableField(value = "school_name")
    private String schoolName;

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

    /**
     * 出售状态（0表示未售，1表示已售）
     */
    @TableField(value = "status")
    private String status;

    /**
     * 审核状态（0表示未审核，1表示通过，2表示未通过）
     */
    @TableField(value = "check")
    private String check;

    /**
     * 审核原因
     */
    @TableField(value = "cause")
    private String cause;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人
     */
    @TableField(value = "update_user",fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}