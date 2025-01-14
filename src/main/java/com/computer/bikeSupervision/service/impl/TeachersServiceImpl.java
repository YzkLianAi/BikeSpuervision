package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Teachers;
import com.computer.bikeSupervision.service.TeachersService;
import com.computer.bikeSupervision.mapper.TeachersMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【teachers(教师表
)】的数据库操作Service实现
* @createDate 2025-01-14 13:47:21
*/
@Service
public class TeachersServiceImpl extends ServiceImpl<TeachersMapper, Teachers>
    implements TeachersService{

}




