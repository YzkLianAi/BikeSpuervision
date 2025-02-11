package com.computer.bikeSupervision.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.service.UsedCarMarketService;
import com.computer.bikeSupervision.mapper.UsedCarMarketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 二手车交易信息服务实现类
 */
@Service
public class UsedCarMarketServiceImpl extends ServiceImpl<UsedCarMarketMapper, UsedCarMarket>
        implements UsedCarMarketService {

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private PlatePassService platePassService;

    @Override
    public void addUsedCarMarket(UsedCarMarketPublishDto usedCarMarketPublishDto, Long currentId) {
        //首先 根据当前人的 查询 其基本信息 获取到其对应的学号和其传递过来的车牌号
        //去PlatePass中查询对应数据 如果有才允许新增
        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //根据当前登陆人 id 查询学生信息
        lambdaQueryWrapper.eq(Students::getId, currentId);
        Students student = studentsService.getOne(lambdaQueryWrapper);

        //获取学号和车牌号
        String studentNumber = student.getStudentNumber();
        String plateNumber = usedCarMarketPublishDto.getPlateNumber();
        //根据学号和车牌号查询
        LambdaQueryWrapper<PlatePass> platePassLambdaQueryWrapper = new LambdaQueryWrapper<>();
        platePassLambdaQueryWrapper.eq(PlatePass::getStudentNumber, studentNumber)
                .eq(PlatePass::getPlateNumber, plateNumber);

        PlatePass platePass = platePassService.getOne(platePassLambdaQueryWrapper);
        //如果为查询到 该学生对应的通行证信息 则禁止发布
        if (platePass == null) {
            throw new CustomException("该车辆信息不属于你，禁止发布");
        }

        //信息拷贝
        UsedCarMarket usedCarMarket = BeanUtil.copyProperties(usedCarMarketPublishDto, UsedCarMarket.class);
        //设置学生信息
        usedCarMarket.setNumber(student.getStudentNumber());
        usedCarMarket.setSchoolName(student.getSchoolName());
        //保存
        this.save(usedCarMarket);
    }

    @Override
    public List<UsedCarMarket> queryUsedCarMarket(Long currentId) {
        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据当前登陆人 id 查询学生信息
        lambdaQueryWrapper.eq(Students::getId, currentId);

        //获取学校信息
        String schoolName = studentsService.getOne(lambdaQueryWrapper).getSchoolName();

        LambdaQueryWrapper<UsedCarMarket> usedCarMarketLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //查询该学校 同时是通过了审核 且 为销售的二手车信息
        usedCarMarketLambdaQueryWrapper.eq(UsedCarMarket::getSchoolName, schoolName)
                .eq(UsedCarMarket::getCheckStatus, '1')
                .eq(UsedCarMarket::getSellStatus, '0');

        return this.list(usedCarMarketLambdaQueryWrapper);
    }

    @Override
    public List<UsedCarMarket> querySellerUsedCarMarket(Long currentId) {
        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据当前登陆人 id 查询学生信息
        lambdaQueryWrapper.eq(Students::getId, currentId);

        //获取学校信息
        Students student = studentsService.getOne(lambdaQueryWrapper);

        LambdaQueryWrapper<UsedCarMarket> usedCarMarketLambdaQueryWrapper = new LambdaQueryWrapper<>();

        usedCarMarketLambdaQueryWrapper.eq(UsedCarMarket::getNumber, student.getStudentNumber())
                .eq(UsedCarMarket::getSchoolName, student.getSchoolName());

        return this.list(usedCarMarketLambdaQueryWrapper);
    }
}




