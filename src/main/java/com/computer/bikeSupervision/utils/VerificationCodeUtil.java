package com.computer.bikeSupervision.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * 生成六位验证码
     *
     * @return 六位验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * 发送验证码到指定邮箱
     *
     * @param email 收件人邮箱
     */
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();
        // 将验证码保存到Redis，设置过期时间为5分钟
        redisTemplate.opsForValue().set("code:" + email, code, 5, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("注册验证码");
        message.setText("您的注册验证码是：" + code + "，有效期为5分钟。");
        javaMailSender.send(message);
    }

    /**
     * 校验验证码
     *
     * @param email 收件人邮箱
     * @param code  验证码
     * @return 校验结果
     */
    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get("code:" + email);
        return code.equals(storedCode);
    }
}