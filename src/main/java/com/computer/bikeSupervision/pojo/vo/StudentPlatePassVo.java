package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

/**
 * 学生获取自身通行证信息返回类
 */
@Data
public class StudentPlatePassVo {
    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 通行证号
     */
    private String passNumber;

    /**
     * 通行证图片云端路径
     */
    private String passImage;
}
