package com.computer.bikeSupervision.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName operate_log
 */
@TableName(value ="operate_log")
@Data
public class OperateLog implements Serializable {
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 操作人id
     */
    @TableField(value = "operate_user")
    private String operateUser;

    /**
     * 操作时间
     */
    @TableField(value = "operate_time")
    private LocalDateTime operateTime;

    /**
     * 操作类
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 操作方法名称
     */
    @TableField(value = "method_name")
    private String methodName;

    /**
     * 传递参数
     */
    @TableField(value = "method_params")
    private String methodParams;

    /**
     * 返回值
     */
    @TableField(value = "return_value")
    private String returnValue;

    /**
     * 耗费时间(ms)
     */
    @TableField(value = "cost_time")
    private Long costTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}