package com.computer.bikeSupervision.pojo.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 违法信息分页查询
 */
@Data
public class ViolationPageVo {
    /**
     * 主键id
     */
    @TableId(value = "id")
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
     * 审核状态(0表示未审核，1表示违法，2表示未违法)
     */
    @TableField(value = "check_status")
    private String checkStatus;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建管理者
     */
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 审核人员
     */
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
