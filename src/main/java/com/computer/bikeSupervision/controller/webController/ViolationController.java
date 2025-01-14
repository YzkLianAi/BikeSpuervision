package com.computer.bikeSupervision.controller.webController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "学生信息管理")
@CrossOrigin
@RequestMapping("/Web/Students")
public class ViolationController {
}
