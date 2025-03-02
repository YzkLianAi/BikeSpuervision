package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车辆转让信息表
 */
@TableName(value ="vehicle_transfer")
@Data
public class VehicleTransfer implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 关联二手交易表id
     */
    @TableField(value = "used_car_id")
    private Long usedCarId;

    /**
     * 学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 卖方学号
     */
    @TableField(value = "seller_number")
    private String sellerNumber;

    /**
     * 买方学号
     */
    @TableField(value = "purchaser_number")
    private String purchaserNumber;

    /**
     * 审核状态（0表示未审核 1表示交易通过 2表示交易未通过）
     */
    @TableField(value = "status")
    private String status;

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
    private String createUser;

    /**
     * 更新人
     */
    @TableField(value = "update_user",fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}