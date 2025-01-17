package com.computer.bikeSupervision.controller.webController;

import com.computer.bikeSupervision.service.AdministratorService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "管理员信息管理")
@CrossOrigin
@RequestMapping("/Web/Administrator")
public class AdministratorController {
    @Autowired
    AdministratorService administratorService;




}
