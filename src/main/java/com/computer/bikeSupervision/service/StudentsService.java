package com.computer.bikeSupervision.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Students;

import java.math.BigDecimal;


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
     */
    String login(StudentLoginDto studentLoginDto);

    /**
     * 分页查询学生信息
     */
    PageBean getStudentsPage(int page, int pageSize, String name,Long currentId);

    /**
     * 注册
     */
    void register(Students newStudent);

    /**
     * 修改学生信息
     */
    void update(Students students);

    /**
     * 根据学号查询学生姓名
     */
    String getStudentNameByStudentNumber(String studentNumber);

    /**
     * 根据学号扣分
     */
    void updateStudentScore(String studentNumber, BigDecimal deductionScore);
}
