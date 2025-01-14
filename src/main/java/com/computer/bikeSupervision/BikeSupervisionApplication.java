package com.computer.bikeSupervision;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//启动类
@SpringBootApplication
//日志记录
@Slf4j
//开启web组件，用于过滤器Filter的使用
@ServletComponentScan
//开启事务注解的支持
@EnableTransactionManagement
public class BikeSupervisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeSupervisionApplication.class, args);
        log.info("项目启动成功");
    }

}
