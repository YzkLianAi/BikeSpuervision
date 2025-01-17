package com.computer.bikeSupervision.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.ViolationMapper;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.pojo.vo.ViolationPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.ViolationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, Violation> implements ViolationService {
    @Autowired
    AdministratorService administratorService;


    @Override
    public PageBean searchPage(int pageNum, int pageSize, String railway, String licencePlate, Long currentId) {
        PageHelper.startPage(pageNum, pageSize);

        //首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        //查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        //获取其对应学校号码
        String schoolName = admin.getSchoolName();
        //查询管理员 所属学校内的违章信息
        LambdaQueryWrapper<Violation> violationWrapper = new LambdaQueryWrapper<>();
        violationWrapper.eq(Violation::getSchoolName, schoolName)
                .eq(StringUtils.isNotEmpty(railway), Violation::getRailway, railway)
                .eq(StringUtils.isNotEmpty(licencePlate), Violation::getLicencePlate, licencePlate)
                .eq(Violation::getCheckStatus, "0");

        //查询信息
        List<Violation> violations = this.list(violationWrapper);
        List<ViolationPageVo> violationPageVos = BeanUtil.copyToList(violations, ViolationPageVo.class);
        //将查询结果转换为Page对象
        Page<ViolationPageVo> violationPage = (Page<ViolationPageVo>) violationPageVos;

        return new PageBean(violationPage.getTotal(), violationPage.getResult());
    }
}




