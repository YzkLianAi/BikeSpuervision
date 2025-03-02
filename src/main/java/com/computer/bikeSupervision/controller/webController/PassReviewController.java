package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.PassReviewAddDto;
import com.computer.bikeSupervision.pojo.entity.*;
import com.computer.bikeSupervision.pojo.vo.PlatePassPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PassReviewService;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PlatePassService platePassService;

    @ApiOperation(value = "通行证审核信息新增")
    @PostMapping("/addPassReview")
    public Result<String> addPassReview(@RequestBody PassReviewAddDto passReviewAddDto) {
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);

        String key = "student:" + currentId;
        // 从Redis中取出学生对象
        String value = (String) redisTemplate.opsForValue().get(key);

        // 将JSON字符串反序列化为学生对象
        Students students = JSONObject.parseObject(value, Students.class);

        log.info("当前登录的学生信息:{}", students);


        if (students == null) {
            //redis中不存在 去数据库中查
            students = studentsService.getById(currentId);
        }

        PassReview passReview = BeanUtil.copyProperties(passReviewAddDto, PassReview.class);

        passReview.setSchoolName(students.getSchoolName());
        passReview.setCollege(students.getCollege());
        passReview.setName(students.getStudentName());
        passReview.setNumber(students.getStudentNumber());

        passReviewService.save(passReview);

        return Result.ok("新增成功");
    }


    @ApiOperation(value = "通行证信息分页查询")
    @GetMapping("/passReviewPage")
    public Result<PageBean> getPassReview(@RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info("分页信息：pageNum: {}, pageSize: {}", pageNum, pageSize);

        //获取当前线程操作人 id
        String currentId = BaseContext.getCurrentId();

        PageBean pageBean = passReviewService.searchPage(pageNum, pageSize, currentId);

        return Result.success(pageBean);
    }


    @ApiOperation(value = "通行证信息审核")
    @PostMapping("/passReviewAudit")
    public Result<String> passReviewAudit(@RequestBody PassReview passReview) throws Exception {
        //就是修改违法信息当中 的 状态字段
        log.info("通行证信息审核：{}", passReview);
        // 获取当前线程操作人 id
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);

        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);
        //管理员权限校验
        if (administrator.getStatus().equals("科员")) {
            // 0是未审核 1是已通过 2是未通过
            // 未通过的话 还会传过来一个原因
            if ("审核通过".equals(passReview.getStatus())) {
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


    @ApiOperation(value = "已登记分页查询")
    @GetMapping("/passPlatePage")
    public Result<PageBean> getPassPlate(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info("分页信息：pageNum: {}, pageSize: {}", pageNum, pageSize);

        //获取当前线程操作人 id
        String currentId = BaseContext.getCurrentId();

        PageBean pageBean = passReviewService.searchPlate(pageNum, pageSize, currentId);

        return Result.success(pageBean);
    }

    //对车辆进行报废修改
    @ApiOperation(value = "车辆报废")
    @PostMapping("/passPlateChange")
    public Result<String> passPlateChange(@RequestBody PlatePassPageVo platePassPageVo) {
        //修改车辆信息的 flag字段
        log.info("车辆报废：{}", platePassPageVo);
        // 获取当前线程操作人 id
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人id：{}", currentId);
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getId, currentId);
        Administrator administrator = administratorService.getOne(lambdaQueryWrapper);
        //管理员权限校验
        if (administrator.getStatus().equals("科员")) {
            //修改状态
            //拷贝
            PlatePass platePass = new PlatePass();
            BeanUtils.copyProperties(platePassPageVo, platePass);
            platePassService.updateById(platePass);
            return Result.success("报废成功");

        } else {
            return Result.error("权限不足");
        }

    }


}
