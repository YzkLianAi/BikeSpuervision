package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.mapper.AdministratorMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【administrator】的数据库操作Service实现
* @createDate 2025-01-14 13:47:21
*/
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator>
    implements AdministratorService{

}




