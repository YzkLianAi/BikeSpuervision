package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.PassReviewMapper;
import com.computer.bikeSupervision.pojo.entity.*;
import com.computer.bikeSupervision.pojo.vo.PlatePassPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PassReviewService;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.ImageProcessorUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class PassReviewServiceImpl extends ServiceImpl<PassReviewMapper, PassReview>
        implements PassReviewService {

    @Autowired
    AdministratorService administratorService;

    @Autowired
    ImageProcessorUtils imageProcessorUtils;

    @Autowired
    PlatePassService platePassService;

    @Autowired
    AliOSSUtils aliOSSUtils;

    @Autowired
    private StudentsService studentsService;

    //通行证信息查询
    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, String currentId) {
        //1.设置分页参数
        PageHelper.startPage(pageNum, pageSize);//设置分页参数

        //首先获取当前操作人信息 查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        //查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        //获取其对应学校号码
        String schoolName = admin.getSchoolName();
        //查询管理员 所属学校内的违章信息
        LambdaQueryWrapper<PassReview> passReviewLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 比对学校信息 和 检查状态
        passReviewLambdaQueryWrapper.eq(PassReview::getSchoolName, schoolName)
                .eq(PassReview::getStatus, "未处理");

        passReviewLambdaQueryWrapper.orderByDesc(PassReview::getUpdateTime);

        Page<PassReview> p = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> this.list(passReviewLambdaQueryWrapper));

        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 审核通过
     */
    @Override
    public void passReviewAudit(PassReview passReview) throws Exception {
        // 生成通行证号
        String passNumber = generatePassNumber(passReview.getNumber(), passReview.getPlateNumber());

        // 保存到车牌管理表中
        PlatePass platePass = new PlatePass();
        platePass.setStudentNumber(passReview.getNumber());
        platePass.setSchoolName(passReview.getSchoolName());
        platePass.setPlateNumber(passReview.getPlateNumber());
        //新增对车辆图片字段
        platePass.setVehicleImage(passReview.getVehicleImage());
        platePass.setPassNumber(passNumber);
        // 先生成 二维码 然后把二维码一起放到 模板当中
        MultipartFile qrImage = platePassService.generateSqOneCode(passReview, passNumber);

        MultipartFile multipartFile = imageProcessorUtils.generatePassImage(passReview.getPlateNumber(), passNumber, qrImage);
        // 调用图片上传工具类
        String uploadUrl = aliOSSUtils.upload(multipartFile);
        platePass.setPassImage(uploadUrl);

        // 保存到数据库
        platePassService.save(platePass);
    }

    //已登记车辆查询
    @Override
    public PageBean searchPlate(Integer pageNum, Integer pageSize, String currentId) {
        // 1. 设置分页参数，只调用一次
        PageHelper.startPage(pageNum, pageSize);

        // 首先获取当前操作人信息，查询其对应学校
        LambdaQueryWrapper<Administrator> adminWrapper = new LambdaQueryWrapper<>();
        adminWrapper.eq(Administrator::getId, currentId);

        // 查询操作人信息
        Administrator admin = administratorService.getOne(adminWrapper);
        // 获取其对应学校号码
        String schoolName = admin.getSchoolName();
        // 查询管理员所属学校内的车牌信息
        LambdaQueryWrapper<PlatePass> platePassLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 比对学校信息
        platePassLambdaQueryWrapper.eq(PlatePass::getSchoolName, schoolName);


        platePassLambdaQueryWrapper.orderByDesc(PlatePass::getFlag)
                .orderByDesc(PlatePass::getUpdateTime);

        // 执行分页查询
        Page<PlatePass> platePassPage = PageHelper.startPage(pageNum, pageSize)
                .doSelectPage(() -> platePassService.list(platePassLambdaQueryWrapper));

        // 获取当前系统时间
        LocalDate now = LocalDate.now();

        List<PlatePassPageVo> voList = new ArrayList<>();
        for (PlatePass platePass : platePassPage.getResult()) {
            PlatePassPageVo vo = new PlatePassPageVo();
            BeanUtils.copyProperties(platePass, vo);

            // 获取起始时间
            LocalDateTime startDateTime = platePass.getCreateTime();
            if (startDateTime != null) {
                // 转换为 LocalDate 进行计算
                LocalDate startDate = startDateTime.toLocalDate();
                // 计算时间差
                Period period = Period.between(startDate, now);
                int years = period.getYears();
                int months = period.getMonths();

                if (years > 0) {
                    vo.setTime(years + "年");
                } else {
                    vo.setTime(months + "个月");
                }
            } else {
                vo.setTime("未知");
            }

            // 根据学号查询学生姓名和学院
            String studentNumber = platePass.getStudentNumber();
            if (studentNumber != null) {
                String studentName = studentsService.getStudentNameByStudentNumber(studentNumber);
                String college = studentsService.getCollegeByStudentNumber(studentNumber);
                vo.setStudentName(studentName);
                vo.setCollege(college);
            }

            voList.add(vo);
        }

        // 手动构建 Page<PlatePassPageVo> 对象
        Page<PlatePassPageVo> platePassPageVoPage = new Page<>();
        platePassPageVoPage.setPageNum(platePassPage.getPageNum());
        platePassPageVoPage.setPageSize(platePassPage.getPageSize());
        platePassPageVoPage.setTotal(platePassPage.getTotal());
        platePassPageVoPage.addAll(voList);

        return new PageBean(platePassPageVoPage.getTotal(), platePassPageVoPage.getResult());
    }

    //获得通行证号码
    private String generatePassNumber(String studentNumber, String plateNumber) {
        // 从学号中取后三位
        String studentNumberSuffix = getLastThreeDigits(studentNumber);
        // 从车牌号中取后三位
        String plateNumberSuffix = getLastThreeDigits(plateNumber);
        // 生成一个三位的随机数
        String randomThreeDigits = generateRandomThreeDigits();

        // 组合成 9 位的通行证号
        return studentNumberSuffix + plateNumberSuffix + randomThreeDigits;
    }

    // 获取字符串 的 后三位
    private String getLastThreeDigits(String str) {
        if (str == null) {
            return "000";
        }
        int length = str.length();
        if (length >= 3) {
            return str.substring(length - 3);
        } else {
            StringBuilder sb = new StringBuilder();
            // 如果长度不足 3 位，前面补 0
            for (int i = 0; i < 3 - length; i++) {
                sb.append('0');
            }
            sb.append(str);
            return sb.toString();
        }
    }

    // 生成一个三位的随机数
    private String generateRandomThreeDigits() {
        Random random = new Random();
        int randomNum = random.nextInt(900) + 100; // 生成 100 - 999 之间的随机数
        return String.valueOf(randomNum);
    }
}




