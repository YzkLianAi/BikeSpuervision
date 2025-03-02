package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 教师表

 * @TableName teachers
 */
@TableName(value ="teachers")
@Data
public class Teachers implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 工号
     */
    @TableField(value = "teacher_number")
    private String teacherNumber;

    /**
     * 教师姓名
     */
    @TableField(value = "teacher_name")
    private String teacherName;

    /**
     * 密码（默认是abc123）
     */
    @TableField(value = "password")
    private String password;

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
     * 学校
     */
    @TableField(value = "school")
    private String school;

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
     * 二维码云端路径
     */
    @TableField(value = "qr_code")
    private String qrCode;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private String createUser;

    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private String updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}