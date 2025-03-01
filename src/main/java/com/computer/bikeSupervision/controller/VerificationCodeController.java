package com.computer.bikeSupervision.controller;

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
    public String sendVerificationCode(@RequestParam String email) {
        verificationCodeService.sendVerificationCode(email);
        return "验证码已发送，请查收邮箱。";
    }

    /**
     * 校验验证码
     *
     * @param email 收件人邮箱
     * @param code  验证码
     * @return 校验结果
     */
    @PostMapping("/verify")
    public boolean verifyCode(@RequestParam String email, @RequestParam String code) {
        return verificationCodeService.verifyCode(email, code);
    }
}