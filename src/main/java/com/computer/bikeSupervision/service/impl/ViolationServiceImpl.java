package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.service.ViolationService;
import com.computer.bikeSupervision.mapper.ViolationMapper;
import org.springframework.stereotype.Service;


@Service
public class ViolationServiceImpl extends ServiceImpl<ViolationMapper, Violation>
    implements ViolationService{

}




