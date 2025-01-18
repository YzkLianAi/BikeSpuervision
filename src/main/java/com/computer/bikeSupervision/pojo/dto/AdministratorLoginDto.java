package com.computer.bikeSupervision.pojo.dto;

import lombok.Data;

@Data
/**
 * 管理员登录传输类
 */
public class AdministratorLoginDto {

    private String adminNumber;    //账号

    private String password;    //密码

    private String schoolName;    //学校名称
}
