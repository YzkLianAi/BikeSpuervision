package com.computer.bikeSupervision.pojo.dto;


import lombok.Data;


/**
 * 学生注册传输对象
 */
@Data
public class StudentRegisterDto {
    //学生姓名
    private String studentName;
    //学生学号
    private String studentNumber;
    //学校
    private String schoolName;
    //学院
    private String college;
}
