package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.PassReviewMapper;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PassReview;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.PassReviewService;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.ImageProcessorUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public PageBean searchPage(Integer pageNum, Integer pageSize, Long currentId) {
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

    // 获取字符串的后三位
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




