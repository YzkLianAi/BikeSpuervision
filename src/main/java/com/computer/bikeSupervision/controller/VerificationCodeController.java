package com.computer.bikeSupervision.controller;

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
     *
     * @param email 收件人邮箱
     * @return 发送结果
     */
    @PostMapping("/send")
    public Result<String> sendVerificationCode(@RequestParam String email) {

        try {
            verificationCodeService.sendVerificationCode(email);

        } catch (Exception e) {
            log.error("验证码发送失败: {}", e.getMessage());
        }
        return Result.success("验证码发送成功");
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