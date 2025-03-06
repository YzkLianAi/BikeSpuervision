package com.computer.bikeSupervision.pojo.dto;


import lombok.Data;


/**
 * 学生注册传输对象
 */
@Data
public class StudentRegisterDto {
    //邮箱
    private String email;
    //密码
    private String password;
    //重复密码
    private String rePassword;
    //验证码
    private String code;
}
