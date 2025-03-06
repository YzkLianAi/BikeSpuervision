package com.computer.bikeSupervision.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
// CORS是一种机制，它使用额外的HTTP头来告诉浏览器
// 让运行在一个origin（域）上的 Web应用被准许访问来自不同源服务器上的指定的资源
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        config.addAllowedOriginPattern("*");
        // 允许任何请求头
        config.addAllowedHeader("*");
        // 允许任何方法（POST、GET 等）
        config.addAllowedMethod("*");
        // 允许携带凭证
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}