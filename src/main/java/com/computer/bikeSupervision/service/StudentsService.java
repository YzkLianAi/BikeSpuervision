package com.computer.bikeSupervision.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.baomidou.mybatisplus.extension.service.IService;


public interface StudentsService extends IService<Students> {

    /**
     * 生成二维码
     * @param id
     * @return
     * @throws Exception
     */
    //String generateSqCode(Long id) throws Exception;

    /**
     * 登录
     * @return
     */
    String login(StudentLoginDto studentLoginDto);

    /**
     * 分页查询学生信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    Page<Students> getStudentsPage(int page, int pageSize, String name);

    /**
     * 注册
     * @param newStudent
     */
    void register(Students newStudent);

    /**
     * 修改学生信息
     * @param students
     */
    void update(Students students);
}
