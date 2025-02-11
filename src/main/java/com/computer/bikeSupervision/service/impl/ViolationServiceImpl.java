package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.ViolationMapper;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PlatePassService;
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
    private AdministratorService administratorService;

    @Autowired
    private PlatePassService platePassService;


    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, String railway, String licencePlate, Long currentId) {
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
        LambdaQueryWrapper<Violation> violationWrapper = new LambdaQueryWrapper<>();
        // 查询的是未审核的信息
        violationWrapper.eq(Violation::getSchoolName, schoolName)
                .eq(StringUtils.isNotEmpty(railway), Violation::getRailway, railway)
                .eq(StringUtils.isNotEmpty(licencePlate), Violation::getLicencePlate, licencePlate)
                .eq(Violation::getCheckStatus, "0");

        //根据 修改时间降序
        violationWrapper.orderByDesc(Violation::getUpdateTime);
        //TODO 为什么不能和之前的项目那样直接转换呢？？？
        //List<Violation> list = this.list(violationWrapper);

        Page<Violation> p = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(violationWrapper));

        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 处理进度查询
     *
     * @param pageNum
     * @param pageSize
     * @param currentId
     * @return
     */
    @Override
    public PageBean searchProgressPage(int pageNum, int pageSize, Long currentId) {
        //首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        //查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        //获取其对应学校号码
        String schoolName = admin.getSchoolName();
        LambdaQueryWrapper<Violation> violationWrapper = new LambdaQueryWrapper<>();

        // 1表示的是 确认违法的 2表示的是未违法 进度查看当中 只查看 已违法的情况下的进度 为0的（表示未处理）
        violationWrapper.eq(Violation::getSchoolName, schoolName)
                .eq(Violation::getCheckStatus, "1")
                .eq(Violation::getDealStatus, "0");

        Page<Violation> p = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(violationWrapper));

        return new PageBean(p.getTotal(), p.getResult());

    }

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
                    .eq(Violation::getDealStatus, "0");

            if (this.count(queryWrapper) > 0) {
                // 如果有未处理的违章记录，直接返回 true
                return true;
            }
        }
        // 如果遍历完所有车牌都没有未处理的违章记录，返回 false
        return false;
    }
}




