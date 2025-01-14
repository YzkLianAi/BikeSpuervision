package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ViolationService extends IService<Violation> {
    /**
     * 查询违规信息
     * @param currentId
     */
    PageBean searchPage(int pageNum, int pageSize, String railway, String licencePlate, Long currentId);




}
