package com.computer.bikeSupervision.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 通行证添加
 */
@Data
public class PassReviewAddDto {
    /**
     * 车牌号
     */
    @TableField(value = "plate_number")
    private String plateNumber;

    /**
     * 学生证/教工证图片
     */
    @TableField(value = "id_card")
    private String idCard;

    /**
     * 交管证明图片
     */
    @TableField(value = "traffic_card")
    private String trafficCard;
}
