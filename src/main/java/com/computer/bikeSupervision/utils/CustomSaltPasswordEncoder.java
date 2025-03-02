package com.computer.bikeSupervision.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomSaltPasswordEncoder {
    //盐值
    private static final String CUSTOM_SALT = "YzkLianAi";
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 加密方法，将自定义盐值与原始密码拼接后进行加密
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public static String encodePassword(String rawPassword) {
        // 将自定义盐值与原始密码拼接
        String passwordWithSalt = rawPassword + CUSTOM_SALT;
        // 使用 BCryptPasswordEncoder 进行加密
        return passwordEncoder.encode(passwordWithSalt);
    }

    /**
     * 验证密码方法，将自定义盐值与输入的密码拼接后进行验证
     *
     * @param rawPassword     输入的原始密码
     * @param encodedPassword 加密后的密码
     * @return 密码是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        // 将自定义盐值与输入的密码拼接
        String passwordWithSalt = rawPassword + CUSTOM_SALT;
        // 使用 BCryptPasswordEncoder 进行验证
        return passwordEncoder.matches(passwordWithSalt, encodedPassword);
    }
}