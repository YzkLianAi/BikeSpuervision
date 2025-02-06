package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.PassReviewAddDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.service.AdministratorService;
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

    @Autowired
    AdministratorService administratorService;

    @ApiOperation(value = "通行证审核信息新增")
    @PostMapping("/addPassReview")
    public Result<String> addPassReview(@RequestBody PassReviewAddDto passReviewAddDto) {
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


    @ApiOperation(value = "通行证信息审核")
    @PostMapping("/passReviewAudit")
    public Result<String> passReviewAudit(@RequestBody PassReview passReview) throws Exception {
        //就是修改违法信息当中 的 状态字段
        log.info("通行证信息审核：{}", passReview);
        // 获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);

        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);
        //权限校验
        if (administrator.getStatus().equals("0")) {
            // 0是未审核 1是已通过 2是未通过
            // 未通过的话 还会传过来一个原因
            if ("1".equals(passReview.getStatus())) {
                // 审核通过
                passReviewService.passReviewAudit(passReview);
            }

            //修改状态
            passReviewService.updateById(passReview);
            return Result.success("审核成功");
        } else {
            return Result.error("权限不足");
        }
    }


}
