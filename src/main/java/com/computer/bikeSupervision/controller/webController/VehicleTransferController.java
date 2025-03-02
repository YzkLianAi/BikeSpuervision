package com.computer.bikeSupervision.controller.webController;


import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.entity.VehicleTransfer;
import com.computer.bikeSupervision.pojo.vo.findTransferVo;
import com.computer.bikeSupervision.service.VehicleTransferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "二手车转让管理")
@CrossOrigin
@RequestMapping("/Web/VehicleTransfer")
public class VehicleTransferController {
    @Autowired
    VehicleTransferService vehicleTransferService;

    /**
     * 新增二手转让交易
     */
    @ApiOperation(value = "新增二手转让交易")
    @PostMapping("/addTransfer")
    public Result<String> addTransfer(@RequestBody VehicleTransfer vehicleTransfer) {
        //获取当前登陆人 id
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}, 转让交易信息：{}", currentId, vehicleTransfer);

        vehicleTransferService.addTransfer(vehicleTransfer, currentId);

        return Result.success("新增成功");
    }

    /**
     * 查询所有二手转让交易
     */
    @ApiOperation(value = "查询所有二手转让交易")
    @GetMapping("/findAllTransfer")
    public Result<List<VehicleTransfer>> findAllTransfer() {
        //获取当前登陆人id
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}", currentId);

        List<VehicleTransfer> vehicleTransfers = vehicleTransferService.findAllTransfer(currentId);

        return Result.success(vehicleTransfers);
    }

    /**
     * 查询单个二手转让详细信息
     */
    @ApiOperation(value = "查询单个二手转让详细信息")
    @GetMapping("/findTransferById")
    public Result<findTransferVo> findTransferById(Long id) {
        // 根据 id 查询二手转让信息
        VehicleTransfer vehicleTransfer = vehicleTransferService.getById(id);
        if (vehicleTransfer == null) {
            throw new CustomException("未找到对应的二手转让信息");
        }

        // 封装信息到 findTransferVo 类
        findTransferVo vo = vehicleTransferService.findTransfer(vehicleTransfer);
        return Result.success(vo);
    }


    /**
     * 审核二手转让交易
     */
    @ApiOperation(value = "审核二手转让交易")
    @PostMapping("/checkTransfer")
    public Result<String> checkTransfer(@RequestBody VehicleTransfer vehicleTransfer) {
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}, 审核二手转让交易：{}", currentId, vehicleTransfer);

        vehicleTransferService.checkTransfer(vehicleTransfer, currentId);

        return Result.success("审核成功");
    }
}
