package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.ViolationMapper;
import com.computer.bikeSupervision.pojo.entity.*;
import com.computer.bikeSupervision.pojo.vo.ViolationPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.service.ViolationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
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

    // 查询违章信息记录
    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, String cause, String licencePlate, String currentId) {
        // 获取当前操作人信息并查询其对应学校
        String schoolName = getSchoolName(currentId);

        // 查询管理员所属学校内的未审核违章信息
        LambdaQueryWrapper<Violation> violationWrapper = buildViolationWrapper(schoolName, cause, licencePlate, "未处理");

        // 开启分页查询
        Page<Violation> page = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(violationWrapper));
        return convertToPageBean(page);
    }



    /**
     * 处理进度查询
     */
    @Override
    public PageBean searchProgressPage(int pageNum, int pageSize, String cause, String licencePlate, String currentId) {
        // 获取当前操作人信息并查询其对应学校
        String schoolName = getSchoolName(currentId);

        // 查询管理员所属学校内的已审核违章信息
        LambdaQueryWrapper<Violation> violationWrapper = buildViolationWrapper(schoolName, cause, licencePlate, "审核通过");

        // 先按照处理状态排序，未处理的排在前面
        violationWrapper.orderByDesc(Violation::getDealStatus)
                // 再按照修改时间降序排序
                .orderByDesc(Violation::getUpdateTime);
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        Page<Violation> page = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(violationWrapper));

        return convertToPageBean(page);
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
            // 根据车牌号查询是否有未处理的违章记录
            if (this.count(new LambdaQueryWrapper<Violation>()
                    .eq(Violation::getLicencePlate, platePass.getPlateNumber())
                    .eq(Violation::getDealStatus, "未处理")) > 0) {
                // 如果有未处理的违章记录，直接返回 true
                return true;
            }
        }
        // 如果遍历完所有车牌都没有未处理的违章记录，返回 false
        return false;
    }

    // 扣分
    @Override
    public void updateViolation(Violation violation) {
        // 拿出里面的车牌号
        String licencePlate = violation.getLicencePlate();
        // 根据车牌号查询学生学号
        String studentNumber = platePassService.getStudentNumberByLicencePlate(licencePlate);
        // 如果查询为空，则没有学生可以进行修改，直接返回
        if (studentNumber == null) {
            return;
        }
        // 拿学号去学生信息表中修改分数
        log.info("学生学号: {}, 扣分: {}", studentNumber, violation.getDeductionScore());
        studentsService.updateStudentScore(studentNumber, violation.getDeductionScore());
    }

    // 根据车牌查询违章信息
    @Override
    public List<Violation> getViolationsByLicensePlates(List<String> licensePlates) {
        if (CollectionUtils.isEmpty(licensePlates)) {
            // 如果车牌号列表为空，直接返回空列表
            return new ArrayList<>();
        }

        LambdaQueryWrapper<Violation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Violation::getLicencePlate, licensePlates);
        return this.list(queryWrapper);
    }

    private String getSchoolName(String currentId) {
        Administrator admin = administratorService.getOne(new LambdaQueryWrapper<Administrator>().eq(Administrator::getId, currentId));
        return admin.getSchoolName();
    }

    // 构建分页查询条件
    private LambdaQueryWrapper<Violation> buildViolationWrapper(String schoolName, String cause, String licencePlate, String checkStatus) {
        return new LambdaQueryWrapper<Violation>()
                .eq(Violation::getSchoolName, schoolName)
                .eq(StringUtils.isNotEmpty(cause), Violation::getCause, cause)
                .eq(StringUtils.isNotEmpty(licencePlate), Violation::getLicencePlate, licencePlate)
                .eq(Violation::getCheckStatus, checkStatus);
    }

    // 将 Page 对象转换为 PageBean 对象
    private PageBean convertToPageBean(Page<Violation> page) {
        List<ViolationPageVo> voList = new ArrayList<>();
        for (Violation violation : page.getResult()) {
            ViolationPageVo vo = new ViolationPageVo();
            BeanUtils.copyProperties(violation, vo);

            // 根据车牌查询 studentNumber
            String studentNumber = platePassService.getStudentNumberByLicencePlate(violation.getLicencePlate());
            // 如果查询成功，则赋值给 vo
            if (studentNumber != null) {
                // 根据 studentNumber 查询学生姓名
                String studentName = studentsService.getStudentNameByStudentNumber(studentNumber);
                vo.setStudentName(studentName);
            }
            voList.add(vo);
        }

        return new PageBean(page.getTotal(), voList);
    }
}