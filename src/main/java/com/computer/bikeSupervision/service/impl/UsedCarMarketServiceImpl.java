package com.computer.bikeSupervision.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.service.UsedCarMarketService;
import com.computer.bikeSupervision.mapper.UsedCarMarketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 二手车交易信息服务实现类
 */
@Service
public class UsedCarMarketServiceImpl extends ServiceImpl<UsedCarMarketMapper, UsedCarMarket>
        implements UsedCarMarketService {

    @Autowired
    private StudentsService studentsService;

    @Override
    public void addUsedCarMarket(UsedCarMarketPublishDto usedCarMarketPublishDto, Long currentId) {
        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //根据当前登陆人 id 查询学生信息
        lambdaQueryWrapper.eq(Students::getId, currentId);
        Students student = studentsService.getOne(lambdaQueryWrapper);
        //信息拷贝
        UsedCarMarket usedCarMarket = BeanUtil.copyProperties(usedCarMarketPublishDto, UsedCarMarket.class);
        //设置学生信息
        usedCarMarket.setNumber(student.getStudentNumber());
        usedCarMarket.setSchoolName(student.getSchoolName());
        //保存
        this.save(usedCarMarket);
    }
}




