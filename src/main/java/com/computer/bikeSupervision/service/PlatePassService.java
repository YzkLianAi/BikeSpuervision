package com.computer.bikeSupervision.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.computer.bikeSupervision.pojo.entity.PassReview;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentPlatePassVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PlatePassService extends IService<PlatePass> {

    /**
     * 生成二维码
     * @param id
     * @return
     */
    String generateSqCode(Long id, String plateNumber) throws Exception;

    /**
     * 学生查询自身通行证信息
     */
    List<StudentPlatePassVo> getStudentPass(Students student);


    /**
     * 查询学生所拥有车牌集合
     */
    List<PlatePass> getPlatePassByStudentNumberAndSchoolName(String studentNumber, String schoolName);

    /**
     * 根据车牌查询学生学号
     */
    String getStudentNumberByLicencePlate(String licencePlate);

    /**
     * 生成通行证二维码
     */
    MultipartFile generateSqOneCode(PassReview passReview, String passNumber) throws Exception;

    /**
     * 根据学号查询车牌集合
     */
    List<String> getLicensePlatesByStudentNumber(String studentNumber);


}
