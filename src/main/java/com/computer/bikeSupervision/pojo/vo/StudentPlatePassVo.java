package com.computer.bikeSupervision.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 学生获取自身通行证信息返回类
 */
@Data
public class StudentPlatePassVo {
    /**
     * 车牌号
     */
    @TableField(value = "plate_number")
    private String plateNumber;

    /**
     * 通行证号
     */
    @TableField(value = "pass_number")
    private String passNumber;

    /**
     * 通行证图片云端路径
     */
    @TableField(value = "pass_image")
    private String passImage;
}
