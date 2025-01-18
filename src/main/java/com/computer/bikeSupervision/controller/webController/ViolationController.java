package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.ViolationAddDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.ViolationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "违法信息管理")
@CrossOrigin
@RequestMapping("/Web/Students")
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
        violationService.save(violation);
        return Result.success("新增成功");
    }

    @ApiOperation(value = "违法信息分页查询")
    @GetMapping("/violationsPage")
    public Result<PageBean> getViolations(@RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          String railway, String licencePlate) {

        log.info("分页信息：pageNum: {}, pageSize: {}, railway: {}, licencePlate: {}", pageNum, pageSize, railway, licencePlate);

        //获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();

        PageBean pageBean = violationService.searchPage(pageNum, pageSize, railway, licencePlate, currentId);

        return Result.success(pageBean);
    }

    @ApiOperation(value = "违法信息审核")
    @PostMapping("/violationAudit")
    public Result<String> violationAudit(@RequestBody Violation violation) {
        //就是修改违法信息当中 的 状态字段
        log.info("违法信息审核：{}", violation);
        // 获取当前线程操作人 id
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);

        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);
        //权限校验
        if (administrator.getStatus().equals("0")){
            violationService.updateById(violation);

            return Result.success("审核成功");
        }else{
            return Result.error("权限不足");
        }

    }

    @ApiOperation(value = "违法信息处理进度查看")
    @GetMapping("/violationProgress")
    public Result<PageBean> violationProgress(){
        return null;
    }
}
