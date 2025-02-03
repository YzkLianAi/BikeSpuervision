package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.PassReviewAddDto;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.service.PassReviewService;
import com.computer.bikeSupervision.service.StudentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "通行证审核管理")
@CrossOrigin
@RequestMapping("/Web/PassReview")
public class PassReviewController {

    @Autowired
    PassReviewService passReviewService;

    @Autowired
    StudentsService studentsService;

    @ApiOperation(value = "通行证审核信息新增")
    @PostMapping("/addPassReview")
    public Result<String> addPassReview(@RequestBody PassReviewAddDto passReviewAddDto){
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);

        //查询当前操作人信息
        LambdaQueryWrapper<Students> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Students::getId, currentId);

        Students students = studentsService.getOne(lambdaQueryWrapper);

        PassReview passReview = BeanUtil.copyProperties(passReviewAddDto, PassReview.class);

        passReview.setSchoolName(students.getSchoolName());
        passReview.setCollege(students.getCollege());
        passReview.setName(students.getStudentName());
        passReview.setNumber(students.getStudentNumber());

        passReviewService.save(passReview);

        return Result.success("新增成功");
    }


    @ApiOperation(value = "通行证信息分页查询")
    @GetMapping("/passReviewPage")
    public Result<PageBean> getPassReview(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info("分页信息：pageNum: {}, pageSize: {}", pageNum, pageSize);

        //获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();

        PageBean pageBean = passReviewService.searchPage(pageNum, pageSize, currentId);

        return Result.success(pageBean);
    }

}
