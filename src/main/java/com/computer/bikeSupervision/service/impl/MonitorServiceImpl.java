package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.pojo.entity.Monitor;
import com.computer.bikeSupervision.service.MonitorService;
import com.computer.bikeSupervision.mapper.MonitorMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【monitor(监控日志
)】的数据库操作Service实现
* @createDate 2025-01-14 13:47:21
*/
@Service
public class MonitorServiceImpl extends ServiceImpl<MonitorMapper, Monitor>
    implements MonitorService{

}




