package com.computer.bikeSupervision.pojo.dto;

import lombok.Data;

/*
 * 学生登录传输对象
 */
@Data
public class StudentLoginDto {

    private String studentNumber;    //学号

    private String password;    //密码

    private String schoolName;    //学校名称
}
