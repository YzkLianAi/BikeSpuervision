package com.computer.bikeSupervision.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.baomidou.mybatisplus.extension.service.IService;


public interface StudentsService extends IService<Students> {

    String generateSqCode(Long id) throws Exception;

    String login(String studentId, String md5Password);

    Page<Students> getStudentsPage(int page, int pageSize, String name);
}
