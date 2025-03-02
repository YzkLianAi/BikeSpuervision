package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 违法信息审核(violation)实体类
 */
@TableName(value = "violation")
@Data
public class Violation implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 车牌号
     */
    @TableField(value = "licence_plate")
    private String licencePlate;

    /**
     * 违法图片
     */
    @TableField(value = "image")
    private String image;

    /**
     * 违法原因（在想要不要存编号）
     */
    @TableField(value = "cause")
    private String cause;



    /**
     * 所属学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 违法路段
     */
    @TableField(value = "railway")
    private String railway;

    /**
     * 审核状态(0表示未审核，1表示已审核)
     */
    @TableField(value = "check_status")
    private String checkStatus;

    /**
     * 扣分
     */
    @TableField(value = "deduction_score")
    private BigDecimal deductionScore;


    /**
     * 处理状态(0表示未处理，1表示已处理)
     */
    @TableField(value = "deal_status")
    private String dealStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建管理者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 审核人员
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

}