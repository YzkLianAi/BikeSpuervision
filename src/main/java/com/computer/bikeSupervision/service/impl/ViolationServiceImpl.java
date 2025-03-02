package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.ViolationMapper;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.pojo.vo.ViolationPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.service.ViolationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, Violation> implements ViolationService {
    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private PlatePassService platePassService;

    @Autowired
    private StudentsService studentsService;

    //查询 违章信息记录
    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, String cause, String licencePlate, String currentId) {
        //1.设置分页参数
        //PageHelper.startPage(pageNum, pageSize);//设置分页参数

        //首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        //查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        //获取其对应学校号码
        String schoolName = admin.getSchoolName();
        //查询管理员 所属学校内的违章信息
        LambdaQueryWrapper<Violation> violationWrapper = new LambdaQueryWrapper<>();
        // 查询的是未审核的信息
        violationWrapper.eq(Violation::getSchoolName, schoolName)
                .eq(StringUtils.isNotEmpty(cause), Violation::getCause, cause)
                .eq(StringUtils.isNotEmpty(licencePlate), Violation::getLicencePlate, licencePlate)
                .eq(Violation::getCheckStatus, "未处理");

        //根据 修改时间降序
        violationWrapper.orderByDesc(Violation::getUpdateTime);

        // 开启分页查询
        Page<Violation> page = PageHelper.startPage(pageNum, pageSize);
        List<Violation> violationList = this.list(violationWrapper);

        List<ViolationPageVo> voList = new ArrayList<>();
        for (Violation violation : violationList) {
            ViolationPageVo vo = new ViolationPageVo();
            BeanUtils.copyProperties(violation, vo);

            // 根据车牌查询 studentNumber
            String studentNumber = platePassService.getStudentNumberByLicencePlate(violation.getLicencePlate());
            if (studentNumber != null) {
                // 根据 studentNumber 查询学生姓名
                String studentName = studentsService.getStudentNameByStudentNumber(studentNumber);
                vo.setStudentName(studentName);
            }
            voList.add(vo);
        }

        // 手动构建 Page<ViolationPageVo> 对象
        Page<ViolationPageVo> violationPageVoPage = new Page<>();
        violationPageVoPage.setPageNum(page.getPageNum());
        violationPageVoPage.setPageSize(page.getPageSize());
        violationPageVoPage.setTotal(page.getTotal());
        violationPageVoPage.addAll(voList);

        return new PageBean(violationPageVoPage.getTotal(), violationPageVoPage.getResult());
    }

    /**
     * 处理进度查询
     */
    @Override
    public PageBean searchProgressPage(int pageNum, int pageSize, String cause, String licencePlate, String currentId) {
        // 首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        // 查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        // 获取其对应学校号码
        String schoolName = admin.getSchoolName();

        LambdaQueryWrapper<Violation> violationWrapper = new LambdaQueryWrapper<>();
        // 1表示的是 确认违法的 2表示的是未违法 进度查看当中 只查看 已违法的情况下的进度 为0的（表示未处理）
        violationWrapper.eq(Violation::getSchoolName, schoolName)
                .eq(StringUtils.isNotEmpty(cause), Violation::getCause, cause)
                .eq(StringUtils.isNotEmpty(licencePlate), Violation::getLicencePlate, licencePlate)
                .eq(Violation::getCheckStatus, "审核通过");

        // 先按照处理状态排序，未处理的（deal_status = "未处理"）排在前面
        violationWrapper.orderByDesc(Violation::getDealStatus)
                // 再按照修改时间降序排序
                .orderByDesc(Violation::getUpdateTime);

        // 开启分页查询
        Page<Violation> page = PageHelper.startPage(pageNum, pageSize);
        List<Violation> violationList = this.list(violationWrapper);

        List<ViolationPageVo> voList = new ArrayList<>();
        for (Violation violation : violationList) {
            ViolationPageVo vo = new ViolationPageVo();
            BeanUtils.copyProperties(violation, vo);

            // 根据车牌查询 studentNumber
            String studentNumber = platePassService.getStudentNumberByLicencePlate(violation.getLicencePlate());
            if (studentNumber != null) {
                // 根据 studentNumber 查询学生姓名
                String studentName = studentsService.getStudentNameByStudentNumber(studentNumber);
                vo.setStudentName(studentName);
            }
            voList.add(vo);
        }

        // 手动构建 Page<ViolationPageVo> 对象
        Page<ViolationPageVo> violationPageVoPage = new Page<>();
        violationPageVoPage.setPageNum(page.getPageNum());
        violationPageVoPage.setPageSize(page.getPageSize());
        violationPageVoPage.setTotal(page.getTotal());
        violationPageVoPage.addAll(voList);

        return new PageBean(violationPageVoPage.getTotal(), violationPageVoPage.getResult());
    }

    /**
     * 根据学号和学校名称查询是否有未处理的违章记录
     */
    @Override
    public boolean hasViolation(String studentNumber, String schoolName) {
        // 根据学号和学校名称查询该学生的车牌信息
        List<PlatePass> platePassList = platePassService.getPlatePassByStudentNumberAndSchoolName(studentNumber, schoolName);
        // 遍历车牌信息列表
        for (PlatePass platePass : platePassList) {
            String plateNumber = platePass.getPlateNumber();
            // 根据车牌号查询是否有未处理的违章记录
            LambdaQueryWrapper<Violation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Violation::getLicencePlate, plateNumber)
                    .eq(Violation::getDealStatus, "未处理");

            if (this.count(queryWrapper) > 0) {
                // 如果有未处理的违章记录，直接返回 true
                return true;
            }
        }
        // 如果遍历完所有车牌都没有未处理的违章记录，返回 false
        return false;
    }

    //扣分
    @Override
    public void updateViolation(Violation violation) {
        //拿出里面的 车牌号
        String licencePlate = violation.getLicencePlate();
        //TODO 注释
        //根据车牌号查询学生学号
        String studentNumber = platePassService.getStudentNumberByLicencePlate(licencePlate);
        //如果查询为空 则没有学生可以进行修改 直接返回 空
        if (studentNumber == null) {
            return;
        }
        //拿学号去 学生信息表中 修改分数
        log.info("学生学号: {}, 扣分: {}", studentNumber, violation.getDeductionScore());
        studentsService.updateStudentScore(studentNumber, violation.getDeductionScore());

    }

    //根据车牌查询违章信息
    @Override
    public List<Violation> getViolationsByLicensePlates(List<String> licensePlates) {
        if (CollectionUtils.isEmpty(licensePlates)) {
            // 如果车牌号列表为空，直接返回空列表
            return null;
        }

        LambdaQueryWrapper<Violation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Violation::getLicencePlate, licensePlates);
        return this.list(queryWrapper);
    }
}




