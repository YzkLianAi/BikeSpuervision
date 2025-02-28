package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通行审核表(PassReview)实体类
 */
@TableName(value ="pass_review")
@Data
public class PassReview implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 所属学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 学院
     */
    @TableField(value = "college")
    private String college;

    /**
     * 姓名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 学号/工号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 0表示学生/1表示教师
     */
    @TableField(value = "identity")
    private String identity;

    /**
     * 车牌号
     */
    @TableField(value = "plate_number")
    private String plateNumber;

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

    /**
     * 0表示未审核/1表示通过/2表示未通过
     */
    @TableField(value = "status")
    private String status;

    /**
     * 未通过的时候提供原因
     */
    @TableField(value = "cause")
    private String cause;

    /**
     * 创建时间
     */

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user",fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user",fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
