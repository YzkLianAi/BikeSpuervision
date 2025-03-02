package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface ViolationService extends IService<Violation> {
    /**
     * 查询违规信息分页
     */
    PageBean searchPage(Integer pageNum, Integer pageSize, String cause, String licencePlate, String currentId);


    /**
     * 查询违规进度
     */
    PageBean searchProgressPage(int pageNum, int pageSize, String cause, String licencePlate, String currentId);


    /**
     * 查询该学生 是否含有未处理的违规信息
     */
    boolean hasViolation(String studentNumber, String schoolName);

    /**
     * 更新违规信息
     */
    void updateViolation(Violation violation);

    /**
     * 根据车牌号查询违规信息
     */
    List<Violation> getViolationsByLicensePlates(List<String> licensePlates);
}
