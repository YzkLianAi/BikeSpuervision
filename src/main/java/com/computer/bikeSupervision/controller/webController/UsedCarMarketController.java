package com.computer.bikeSupervision.controller.webController;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.UsedCarMarketPublishDto;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.entity.UsedCarMarket;
import com.computer.bikeSupervision.service.StudentsService;
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

    @Autowired
    StudentsService studentsService;

    @PostMapping("/publish")
    public Result<String> publishUsedCarInfo(@RequestBody UsedCarMarketPublishDto usedCarMarketPublishDto) {
        //获取当前登陆人 id
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //根据当前登陆人 id 查询学生信息
        lambdaQueryWrapper.eq(Students::getId,currentId);
        Students student = studentsService.getOne(lambdaQueryWrapper);
        //信息拷贝
        UsedCarMarket usedCarMarket = BeanUtil.copyProperties(usedCarMarketPublishDto, UsedCarMarket.class);
        //设置学生信息
        usedCarMarket.setNumber(student.getStudentNumber());
        usedCarMarket.setSchoolName(student.getSchoolName());
        //保存
        usedCarMarketService.save(usedCarMarket);

        return Result.success("发布成功");
    }

}
