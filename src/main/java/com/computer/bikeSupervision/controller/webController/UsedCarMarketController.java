package com.computer.bikeSupervision.controller.webController;


import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.service.UsedCarMarketService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "二手交易信息发布管理")
@CrossOrigin
@RequestMapping("/Web/UsedCarMarket")
public class UsedCarMarketController {

    @Autowired
    UsedCarMarketService usedCarMarketService;


    @PostMapping("/publish")
    public Result<String> publishUsedCarInfo(@RequestBody UsedCarMarketPublishDto usedCarMarketPublishDto) {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人：{}, 发布二手交易信息：{}", currentId, usedCarMarketPublishDto);
        // 新增发布信息
        usedCarMarketService.addUsedCarMarket(usedCarMarketPublishDto, currentId);

        return Result.success("发布成功");
    }

}
