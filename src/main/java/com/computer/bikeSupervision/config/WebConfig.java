package com.computer.bikeSupervision.config;

import com.computer.bikeSupervision.interceprot.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin
@Configuration //拦截器的配置类 其中引入了拦截器对象
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加不需要拦截的路径
        log.info("扩展拦截器...");
        List<String> list = new ArrayList<>();
        //这里添加不需要被拦截的路径
        list.add("/Students/login");

        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns(list);
        //拦截所有路径 除了 那些指定的不需要被拦截的路径
    }
}
