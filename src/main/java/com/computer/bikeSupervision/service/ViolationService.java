package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ViolationService extends IService<Violation> {
    /**
     * 查询违规信息分页
     */
    PageBean searchPage(Integer pageNum, Integer pageSize, String railway, String licencePlate, Long currentId);


    /**
     * 查询违规进度
     */
    PageBean searchProgressPage(int pageNum, int pageSize, Long currentId);


    /**
     * 查询该学生 是否含有未处理的违规信息
     */
    boolean hasViolation(String studentNumber, String schoolName);
}
