package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;


@TableName(value ="violation")
@Data
public class Violation implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 处理状态(0表示未处理，1表示已处理)
     */
    @TableField(value = "deal_status")
    private String dealStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建管理者
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 审核人员
     */
    @TableField(value = "update_user")
    private Long updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}