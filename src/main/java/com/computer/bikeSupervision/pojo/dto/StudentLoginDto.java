package com.computer.bikeSupervision.pojo.dto;

import lombok.Data;

@Data
/*
 * 学生登录传输对象
 */
public class StudentLoginDto {

    private String studentNumber;    //学号

    private String password;    //密码
}
