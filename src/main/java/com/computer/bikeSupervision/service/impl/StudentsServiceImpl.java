package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import org.springframework.stereotype.Service;


@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

}




