package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车牌信息管理表
 */
@TableName(value = "plate_pass")
@Data
public class PlatePass implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 学号
     */
    @TableField(value = "student_number")
    private String studentNumber;

    /**
     * 所属学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 车牌号
     */
    @TableField(value = "plate_number")
    private String plateNumber;

    /**
     * 通行证号
     */
    @TableField(value = "pass_number")
    private String passNumber;

    /**
     * 车辆图片
     */
    @TableField(value = "vehicle_image")
    private String vehicleImage;

    /**
     * 通行证图片云端路径
     */
    @TableField(value = "pass_image")
    private String passImage;

    /**
     * 二维码云端路径
     */
    @TableField(value = "qr_code")
    private String qrCode;

    /**
     * 使用状态
     */
    @TableField(value = "flag")
    private String flag;


    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 更新人
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}