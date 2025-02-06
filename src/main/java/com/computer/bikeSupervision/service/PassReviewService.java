package com.computer.bikeSupervision.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;


public interface PassReviewService extends IService<PassReview> {

    /**
     * 查询分页
     */
    PageBean searchPage(Integer pageNum, Integer pageSize, Long currentId);

    /**
     * 审核通过
     */
    void passReviewAudit(PassReview passReview) throws Exception;
}
