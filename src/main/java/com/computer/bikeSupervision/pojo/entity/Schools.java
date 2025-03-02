package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学校信息表
 *
 * @TableName schools
 */
@TableName(value = "schools")
@Data
public class Schools implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 学校编号
     */
    @TableField(value = "school_number")
    private String schoolNumber;

    /**
     * 学校名称
     */
    @TableField(value = "school_name")
    private String schoolName;

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