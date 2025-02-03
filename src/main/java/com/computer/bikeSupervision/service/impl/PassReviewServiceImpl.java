package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.PassReviewMapper;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PassReviewService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PassReviewServiceImpl extends ServiceImpl<PassReviewMapper, PassReview>
        implements PassReviewService {

    @Autowired
    AdministratorService administratorService;

    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, Long currentId) {
        //1.设置分页参数
        PageHelper.startPage(pageNum, pageSize);//设置分页参数

        //首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        //查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        //获取其对应学校号码
        String schoolName = admin.getSchoolName();
        //查询管理员 所属学校内的违章信息
        LambdaQueryWrapper<PassReview> passReviewLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 比对学校信息 和 检查状态
        passReviewLambdaQueryWrapper.eq(PassReview::getSchoolName, schoolName)
                .eq(PassReview::getStatus, 0);

        passReviewLambdaQueryWrapper.orderByDesc(PassReview::getUpdateTime);

        Page<PassReview> p = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(passReviewLambdaQueryWrapper));

        return new PageBean(p.getTotal(), p.getResult());
    }
}




