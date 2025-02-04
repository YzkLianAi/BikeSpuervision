package com.computer.bikeSupervision.pojo.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生基础信息 分页查询
 */
@Data
public class StudentsPageVo {
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
     * 性别
     */
    @TableField(value = "gender")
    private String gender;

    /**
     * 学院
     */
    @TableField(value = "college")
    private String college;

    /**
     * 是否拥有电瓶车
     */
    private boolean hasBike;

    /**
     * 是否拥有通行证
     */
    private boolean hasPass;

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
