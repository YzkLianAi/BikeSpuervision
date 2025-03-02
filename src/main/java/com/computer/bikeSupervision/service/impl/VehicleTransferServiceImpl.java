package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.mapper.VehicleTransferMapper;
import com.computer.bikeSupervision.pojo.entity.*;
import com.computer.bikeSupervision.pojo.vo.findTransferVo;
import com.computer.bikeSupervision.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 车辆转让信息服务实现类
 */
@Service
public class VehicleTransferServiceImpl extends ServiceImpl<VehicleTransferMapper, VehicleTransfer>
        implements VehicleTransferService {

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private UsedCarMarketService usedCarMarketService;

    @Autowired
    private PlatePassService platePassService;

    @Autowired
    private ViolationService violationService;

    /**
     * 新增二手转让信息
     */
    @Override
    public void addTransfer(VehicleTransfer vehicleTransfer, String currentId) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getId, currentId);

        Students student = studentsService.getOne(wrapper);

        vehicleTransfer.setSchoolName(student.getSchoolName());
        vehicleTransfer.setSellerNumber(student.getStudentNumber());

        this.save(vehicleTransfer);

    }

    /**
     * 查询所有二手转让信息
     */
    @Override
    public List<VehicleTransfer> findAllTransfer(String currentId) {
        //设置条件查询器
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);
        //查询当前登录的管理员信息
        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);
        //判断当前管理是否存在
        if (administrator == null) {
            throw new CustomException("非管理员用户登录");
        }
        //管理员权限校验
        if (administrator.getStatus().equals("科员")) {
            LambdaQueryWrapper<VehicleTransfer> wrapper = new LambdaQueryWrapper<>();
            //比对审核状态 和 学校名称
            wrapper.eq(VehicleTransfer::getStatus, "未处理")
                    .eq(VehicleTransfer::getSchoolName, administrator.getSchoolName());
            return this.list(wrapper);
        } else {
            throw new CustomException("权限不足");
        }

    }

    /**
     * 审核二手转让交易
     */
    @Override
    public void checkTransfer(VehicleTransfer vehicleTransfer, String currentId) {
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);

        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);

        if (administrator == null) {
            throw new CustomException("非管理员用户登录");
        }

        if (administrator.getStatus().equals("科员")) {
            this.updateById(vehicleTransfer);
        } else {
            throw new CustomException("权限不足");
        }


    }

    /**
     * 查询二手转让详细信息
     */
    @Override
    public findTransferVo findTransfer(VehicleTransfer vehicleTransfer) {
        // 根据二手转让信息中的二手发布信息 id 查询二手发布信息
        Long usedCarMarketId = vehicleTransfer.getUsedCarId();
        UsedCarMarket usedCarMarket = usedCarMarketService.getById(usedCarMarketId);

        //获取学校名称
        String schoolName = vehicleTransfer.getSchoolName();

        // 获取买方和卖方 学号
        String purchaserNumber = vehicleTransfer.getPurchaserNumber();
        String sellerNumber = vehicleTransfer.getSellerNumber();

        // 查询卖方拥有车辆数量和是否有违法记录
        List<PlatePass> sellerList = platePassService.getPlatePassByStudentNumberAndSchoolName(sellerNumber, schoolName);
        int sellerVehicleCount = sellerList.size();
        boolean sellerHasViolation = violationService.hasViolation(sellerNumber, schoolName);

        // 查询买方拥有车辆数量和是否有违法记录
        List<PlatePass> buyerList = platePassService.getPlatePassByStudentNumberAndSchoolName(purchaserNumber, schoolName);
        int buyerVehicleCount = buyerList.size();
        boolean buyerHasViolation = violationService.hasViolation(purchaserNumber, schoolName);

        findTransferVo vo = new findTransferVo();
        vo.setUsedCarMarket(usedCarMarket);
        vo.setSellerVehicleCount(sellerVehicleCount);
        vo.setSellerHasViolation(sellerHasViolation);
        vo.setBuyerVehicleCount(buyerVehicleCount);
        vo.setBuyerHasViolation(buyerHasViolation);

        return vo;
    }
}




