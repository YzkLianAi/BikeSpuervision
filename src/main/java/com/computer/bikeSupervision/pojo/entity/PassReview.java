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
