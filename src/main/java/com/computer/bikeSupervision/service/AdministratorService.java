package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.dto.AdministratorLoginDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;


public interface AdministratorService extends IService<Administrator> {

    /**
     * 管理员登录
     * @param administratorLoginDto
     * @return
     */
    String login(AdministratorLoginDto administratorLoginDto, HttpServletRequest request);

    /**
     * 管理员注册
     * @param administrator
     */
    void register(Administrator administrator);

    /**
     * 管理员信息修改
     * @param administrator
     */
    void update(Administrator administrator);
}
