package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

@Data
/*
  专用学生二维码信息类
 */
public class PlatePassSQVo {
    //学号
    private String studentNumber;

    //车牌
    private String plateNumber;

    //通行证
    private String passNumber;
}
