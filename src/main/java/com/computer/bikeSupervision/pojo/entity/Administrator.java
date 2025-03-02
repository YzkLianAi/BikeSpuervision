package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName administrator
 */
@TableName(value = "administrator")
@Data
public class Administrator implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 管理员账号
     */
    @TableField(value = "admin_number")
    private String adminNumber;

    /**
     * 管理员姓名
     */
    @TableField(value = "admin_name")
    private String adminName;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 电话号码
     */
    @TableField(value = "telephone")
    private String telephone;

    /**
     * 所属学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 权限
     */
    @TableField(value = "status")
    private String status;

    /**
     * 权限
     */
    @TableField(value = "dept")
    private String dept;

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