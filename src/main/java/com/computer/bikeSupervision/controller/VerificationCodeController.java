package com.computer.bikeSupervision.controller;

import com.alibaba.fastjson.JSONObject;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.VerificationCodeUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "邮件管理接口")
@RequestMapping("/verification")
public class VerificationCodeController {

    @Autowired
    private VerificationCodeUtil verificationCodeService;

    /**
     * 发送验证码到指定邮箱
     */
    @PostMapping("/send")
    public Result<String> sendVerificationCode(@RequestParam String email) {
        log.info("接收到的邮箱数据: {}", email);
        String actualEmail = email;
        try {
            // 尝试将传入的数据解析为 JSON 对象
            JSONObject jsonObject = JSONObject.parseObject(email);
            // 从 JSON 对象中提取邮箱地址
            actualEmail = jsonObject.getString("email");
        } catch (Exception e) {
            // 解析失败，说明传入的不是 JSON 字符串，直接使用原始数据
            log.info("传入的数据不是 JSON 格式，直接使用原始数据作为邮箱地址");
        }

        try {
            // 发送验证码
            verificationCodeService.sendVerificationCode(actualEmail);
            return Result.ok("验证码发送成功");
        } catch (Exception e) {
            log.error("验证码发送失败: {}", e.getMessage());
            throw new CustomException("验证码发送失败");
        }
    }

    /**
     * 校验验证码
     *
     * @param email 收件人邮箱
     * @param code  验证码
     * @return 校验结果
     */
    @PostMapping("/verify")
    public Result<String> verifyCode(@RequestParam String email, @RequestParam String code) {

        if (verificationCodeService.verifyCode(email, code)) {
            return Result.success("验证码校验成功");
        } else {
            return Result.error("验证码校验失败");
        }
    }
}