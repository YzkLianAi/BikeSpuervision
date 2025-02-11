package com.computer.bikeSupervision.controller.webController;


import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
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


    /**
     * 新增二手交易信息
     * @param usedCarMarketPublishDto
     * @return
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
     * 查询已发布二手交易信息 -> 买方针对所有信息的查询 和 卖方针对自己发布的二手信息的查询
     */

    /**
     * 卖方查询自己已发布的二手交易信息
     */
    @ApiOperation(value = "卖方查询自己已发布的二手信息")
    @GetMapping("/querySellerUsedCarInfo")
    public Result querySellerUsedCarInfo() {
        return Result.success("查询成功");
    }

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






}
