package com.computer.bikeSupervision.controller.webController;

import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.service.ViolationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@Api(tags = "违法信息管理")
@CrossOrigin
@RequestMapping("/Web/Students")
public class ViolationController {

    @Resource
    private ViolationService violationService;

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


}
