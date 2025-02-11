package com.computer.bikeSupervision.service;

import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentPlatePassVo;

import java.util.List;


public interface PlatePassService extends IService<PlatePass> {

    /**
     * 生成二维码
     * @param id
     * @return
     */
    String generateSqCode(Long id) throws Exception;

    /**
     * 学生查询自身通行证信息
     */
    List<StudentPlatePassVo> getStudentPass(Students student);


    /**
     * 查询学生所拥有车牌集合
     */
    List<PlatePass> getPlatePassByStudentNumberAndSchoolName(String studentNumber, String schoolName);
}
