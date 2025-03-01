package com.computer.bikeSupervision.pojo.vo;


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
    private Long id;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 性别
     */
    private String gender;

    /**
     * 学院
     */
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 更新人
     */
    private Long updateUser;

}
