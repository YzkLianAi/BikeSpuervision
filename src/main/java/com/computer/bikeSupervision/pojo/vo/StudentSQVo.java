package com.computer.bikeSupervision.pojo.vo;

import lombok.Data;

@Data
/*
  学生二维码信息类
 */
public class StudentSQVo {
    //姓名
    private String studentName;

    //学号
    private String studentNumber;

    //车牌
    private String plateNumber;

    //通行证
    private String passNumber;
}
