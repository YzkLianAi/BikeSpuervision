package com.computer.bikeSupervision.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;


public interface PassReviewService extends IService<PassReview> {

    PageBean searchPage(Integer pageNum, Integer pageSize, Long currentId);

}
