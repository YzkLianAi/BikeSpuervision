package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通行审核表(PassReview)实体类
 */
@TableName(value ="pass_review")
@Data
public class PassReview implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 学生证/教工证图片
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 交管证明图片
     */
    @TableField(value = "traffic_card")
    private String trafficCard;

    /**
     * 0表示未审核/1表示通过/2表示通过
     */
    @TableField(value = "status")
    private String status;

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
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(value = "update_user")
    private Long updateUser;
}
