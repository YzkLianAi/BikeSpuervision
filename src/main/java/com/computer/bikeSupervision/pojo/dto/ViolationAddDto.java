package com.computer.bikeSupervision.pojo.dto;

import lombok.Data;

/**
 * 新增违章信息
 */
@Data
public class ViolationAddDto {
    /**
     * 车牌号
     */
    private String licencePlate;

    /**
     * 违法图片
     */
    private String image;

    /**
     * 违法原因（在想要不要存编号）
     */
    private String cause;

    /**
     * 所属学校
     */
    private String schoolName;

    /**
     * 违法路段
     */
    private String railway;

}
