package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Schools;
import com.computer.bikeSupervision.service.SchoolsService;
import com.computer.bikeSupervision.mapper.SchoolsMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【schools(学校信息表
)】的数据库操作Service实现
* @createDate 2025-01-14 13:47:21
*/
@Service
public class SchoolsServiceImpl extends ServiceImpl<SchoolsMapper, Schools>
    implements SchoolsService{

}




