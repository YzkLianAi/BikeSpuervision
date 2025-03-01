package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.ViolationAddDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.pojo.vo.ViolationOneVo;
import com.computer.bikeSupervision.pojo.vo.ViolationPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.ViolationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@Api(tags = "违法信息管理")
@CrossOrigin
@RequestMapping("/Web/Violation")
public class ViolationController {

    @Autowired
    private ViolationService violationService;

    @Autowired
    private AdministratorService administratorService;

    @ApiOperation(value = "违法信息新增")
    @PostMapping("/violation")
    public Result<String> addViolation(@RequestBody ViolationAddDto violationAddDto) {
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);

        log.info("新增违法信息：{}", violationAddDto);

        // 数据拷贝
        Violation violation = BeanUtil.copyProperties(violationAddDto, Violation.class);
        //生成5~10分的扣分
        violation.setDeductionScore(BigDecimal.valueOf(Math.random() * 5 + 5));

        violationService.save(violation);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "违法信息分页查询")
    @GetMapping("/violationsPage")
    public Result<PageBean> getViolations(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          String cause, String licencePlate) {

        log.info("分页信息：pageNum: {}, pageSize: {}, railway: {}, licencePlate: {}", pageNum, pageSize, cause, licencePlate);

        //获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();

        PageBean pageBean = violationService.searchPage(pageNum, pageSize, cause, licencePlate, currentId);

        return Result.success(pageBean);
    }

    @ApiOperation(value = "违法信息审核")
    @PostMapping("/violationAudit")
    public Result<String> violationAudit(@RequestBody ViolationPageVo violationPageVo) {
        //就是修改违法信息当中 的 状态字段
        log.info("违法信息审核：{}", violationPageVo);
        // 获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);

        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);

        Violation violation = new Violation();
        //拷贝
        BeanUtils.copyProperties(violationPageVo, violation);
        //管理员权限校验
        if (administrator.getStatus().equals("科员")) {

            //审核流程 审批通过后要扣分
            if (violation.getCheckStatus().equals("审核通过")) {
                violationService.updateViolation(violation);
            }
            violationService.updateById(violation);

            return Result.success("审核成功");
        } else {
            return Result.error("权限不足");
        }

    }

    @ApiOperation(value = "违法信息处理进度查看")
    @GetMapping("/violationProgress")
    public Result<PageBean> violationProgress(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              String cause, String licencePlate) {

        //获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("分页信息：pageNum: {}, pageSize: {}, railway: {}, licencePlate: {}", pageNum, pageSize, cause, licencePlate);
        PageBean pageBean = violationService.searchProgressPage(pageNum, pageSize, cause, licencePlate, currentId);

        return Result.success(pageBean);
    }

    @ApiOperation(value = "查询单条违章记录")
    @GetMapping("/violationOne")
    public Result<ViolationOneVo> violationOne() {

        LambdaQueryWrapper<Violation> violationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        violationLambdaQueryWrapper.eq(Violation::getId, "1895394236656269954");

        Violation violation = violationService.getOne(violationLambdaQueryWrapper);

        ViolationOneVo violationOneVo = new ViolationOneVo();
        BeanUtils.copyProperties(violation, violationOneVo);
        violationOneVo.setVehicleType("电动自行车");

        return Result.success(violationOneVo);

    }
}
