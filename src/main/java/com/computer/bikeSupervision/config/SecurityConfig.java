package com.computer.bikeSupervision.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// 配置 Spring Security 的类。Spring Security 是一个功能强大的安全框架，用于保护基于 Spring 的应用程序
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll() // 允许所有请求通过，可根据实际情况修改
                .and()
                .csrf().disable() // 禁用 CSRF 保护，可根据实际情况修改
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 无状态会话管理

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}