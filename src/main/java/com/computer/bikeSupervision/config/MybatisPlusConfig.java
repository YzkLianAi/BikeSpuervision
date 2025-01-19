package com.computer.bikeSupervision.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;

//配置一下Mybatis-plus的分页插件
public class MybatisPlusConfig {

    //让spring来管理它
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //将MybatisPlusInterceptor这个对象new一下成mybatisPlusInterceptor
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //然后再调用mybatisPlusInterceptor里面的方法
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //最后返回这个对象本来方法定义上是MybatisPlusInterceptor，但是我们这里重新new了一下，所以返回的是mybatisPlusInterceptor
        return mybatisPlusInterceptor;
    }
}
