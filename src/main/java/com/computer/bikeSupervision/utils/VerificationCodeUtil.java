package com.computer.bikeSupervision.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VerificationCodeUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        log.info("发送验证码到邮箱: {}, 验证码: {}", email, code);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            // 处理邮件发送异常，例如记录日志或抛出自定义异常
            e.printStackTrace();
            // 这里可以根据实际需求进行更详细的异常处理
        }
    }

    /**
     * 校验验证码
     *
     * @param email 收件人邮箱
     * @param code  验证码
     * @return 校验结果
     */
    public boolean verifyCode(String email, String code) {
        String storedCode = (String) redisTemplate.opsForValue().get("code:" + email);
        return code.equals(storedCode);
    }
}