package com.computer.bikeSupervision.controller.webController;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.UsedCarMarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "二手交易信息发布管理")
@CrossOrigin
@RequestMapping("/Web/UsedCarMarket")
public class UsedCarMarketController {

    @Autowired
    UsedCarMarketService usedCarMarketService;

    @Autowired
    AdministratorService administratorService;

    /**
     * 新增二手交易信息
     *
     */
    @ApiOperation(value = "新增二手交易信息")
    @PostMapping("/publish")
    public Result<String> publishUsedCarInfo(@RequestBody UsedCarMarketPublishDto usedCarMarketPublishDto) {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}, 发布二手交易信息：{}", currentId, usedCarMarketPublishDto);


        // 新增发布信息
        usedCarMarketService.addUsedCarMarket(usedCarMarketPublishDto, currentId);

        return Result.success("发布成功");
    }

    /**
     * 卖方查询自己已发布的二手交易信息
     */
    @ApiOperation(value = "卖方查询自己已发布的二手信息")
    @GetMapping("/querySellerUsedCarInfo")
    public Result<List<UsedCarMarket>> querySellerUsedCarInfo() {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();

        log.info("当前操作人：{}, 查询自己已发布的二手信息", currentId);
        //查询该学生学校的二手交易信息
        List<UsedCarMarket> usedCarMarketList = usedCarMarketService.querySellerUsedCarMarket(currentId);

        return Result.success(usedCarMarketList);
    }

    /**
     * 卖方查询自己已发布单个的详细信息
     */
    @ApiOperation(value = "卖方查询自己已发布单个的详细信息")
    @GetMapping("/querySellerUsedCar/{id}")
    public Result<UsedCarMarket> querySellerUsedCar(@PathVariable Long id) {
        log.info("当前查询的信息id：{}", id);

        LambdaQueryWrapper<UsedCarMarket> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UsedCarMarket::getId, id);

        return Result.success(usedCarMarketService.getOne(queryWrapper));
    }


    /**
     * 买方查询所有发布的且为销售的二手信息
     */
    @ApiOperation(value = "买法查询所有发布的且为销售的二手信息")
    @GetMapping("/queryPurchaserUsedCarInfo")
    public Result<List<UsedCarMarket>> queryPurchaserUsedCarInfo() {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}, 查询所有发布的且为销售的二手信息", currentId);

        //查询该学生学校的二手交易信息
        List<UsedCarMarket> usedCarMarketList = usedCarMarketService.queryUsedCarMarket(currentId);

        return Result.success(usedCarMarketList);
    }

    /**
     * 管理员审核发布信息
     */
    @ApiOperation(value = "管理员审核发布信息")
    @PostMapping("/auditPublishInfo")
    public Result<String> auditPublishInfo(@RequestBody UsedCarMarket usedCarMarket) {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}", currentId);

        //身份校验
        LambdaQueryWrapper<Administrator> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Administrator::getId, currentId);
        Administrator administrator = administratorService.getOne(queryWrapper);

        if (administrator == null) {
            throw new CustomException("非管理员用户登录");
        }
        //管理员权限校验
        if (administrator.getStatus().equals("科员")) {
            usedCarMarketService.updateById(usedCarMarket);
            return Result.success("审核成功");
        }else{
            return Result.error("权限不足");
        }
    }

}
