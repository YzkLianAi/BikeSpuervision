package com.computer.bikeSupervision;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BikeSupervisionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BikeSupervisionApplication.class, args);
        log.info("项目启动成功");
    }

}
