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
 * 学生表(Students)实体类
 */
@TableName(value ="students")
@Data
public class Students implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 学生姓名
     */
    @TableField(value = "student_name")
    private String studentName;

    /**
     * 学号
     */
    @TableField(value = "student_number")
    private String studentNumber;

    /**
     * 密码（默认是123456）
     */
    @TableField(value = "password")
    private String password;

    /**
     * 分数
     */
    @TableField(value = "score")
    private BigDecimal score;

    /**
     * 性别
     */
    @TableField(value = "gender")
    private String gender;


    /**
     * 电话
     */
    @TableField(value = "telephone")
    private String telephone;

    /**
     * 学校名称
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 学院
     */
    @TableField(value = "college")
    private String college;

    /**
     * 头像（云端url）
     */
    @TableField(value = "image")
    private String image;


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
}