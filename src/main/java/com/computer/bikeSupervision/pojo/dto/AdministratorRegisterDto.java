package com.computer.bikeSupervision.pojo.dto;

import lombok.Data;


/**
 * 管理员注册
 */
@Data
public class AdministratorRegisterDto {
    //管理员姓名
    private String adminName;
    //账号
    private String adminNumber;
    //学校
    private String schoolName;

}
