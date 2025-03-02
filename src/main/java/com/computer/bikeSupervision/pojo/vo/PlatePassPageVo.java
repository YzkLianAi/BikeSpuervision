package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PlatePassPageVo {
    /**
     * 主键id
     */
    private String id;

    /**
     * 学号
     */
    private String studentNumber;

    /**
     * 姓名
     */
    private String studentName;

    /**
     * 所属学校
     */
    private String college;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 通行证号
     */
    private String passNumber;

    /**
     * 车辆图片
     */
    private String image;

    /**
     * 通行证图片云端路径
     */
    private String passImage;


    /**
     * 创建时间
     */
    private String time;


    /**
     * 使用状态
     */
    private String flag;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
