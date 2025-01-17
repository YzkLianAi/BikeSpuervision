package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 监控日志

 * @TableName monitor
 */
@TableName(value ="monitor")
@Data
public class Monitor implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 监控日期
     */
    @TableField(value = "date")
    private Date date;

    /**
     * 监控路段
     */
    @TableField(value = "railway")
    private String railway;

    /**
     * 监控所属学校
     */
    @TableField(value = "school_name")
    private String schoolName;

    /**
     * 创建日期
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改日期
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}