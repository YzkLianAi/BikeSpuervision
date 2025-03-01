package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 违法信息分页查询
 */
@Data
public class ViolationPageVo {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 车牌号
     */
    private String licencePlate;

    /**
     * 违法图片
     */
    private String image;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 违法原因（在想要不要存编号）
     */
    private String cause;


    /**
     * 违法路段
     */
    private String railway;

    /**
     * 审核状态(0表示未审核，1表示违法，2表示未违法)
     */
    private String checkStatus;

    /**
     * 处理状态(0表示未处理，1表示已处理)
     */
    private String dealStatus;

    /**
     * 扣分
     */
    private BigDecimal deductionScore;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建管理者
     */
    private Long createUser;

    /**
     * 审核人员
     */
    private Long updateUser;

}
