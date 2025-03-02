package com.computer.bikeSupervision.common;

import lombok.Getter;

/**
 * @author Panda
 * @date 2022/6/17 13:28
 */

@Getter
public enum ResultCode {

    /**
     * 整体结果
     */
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),

    /**
     * 非法操作
     */
    SERVICE_ERROR(203, "服务异常"),
    ILLEGAL_REQUEST( 204, "非法请求"),
    ARGUMENT_VALID_ERROR(206, "参数校验错误"),


    /**
     * 登录相关
     */
    REPETITION_USERNAME(226,"用户名重复"),
    LOGIN_ERROR(207, "用户名或密码错误"),
    OTHER_CLIENTS_LOGGED_IN(208, "已在其他端登陆"),
    PERMISSION(209, "没有权限"),
    LOGIN_CODE(222,"长时间未操作,会话已失效,请刷新页面后重试!"),
    CODE_ERROR(223,"验证码错误!"),
    TOKEN_ERROR(224,"Token非法!"),
    TOKEN_EXPIRED(225,"Token过期"),
    REGISTER_ERROR(226,"用户名重复"),
    NOT_LOGIN(227,"未登录"),

    /**
     * 秒杀
     */
    SECKILL_NO_START(210, "秒杀还没开始"),
    SECKILL_RUN(211, "正在排队中"),
    SECKILL_NO_PAY_ORDER(212, "您有未支付的订单"),
    PAY_RUN(205, "支付中"), //可同时用于支付模块
    SECKILL_FINISH(213, "已售罄"),
    SECKILL_END(214, "秒杀已结束"),
    SECKILL_SUCCESS(215, "抢单成功"),
    SECKILL_FAIL(216, "抢单失败"),
    SECKILL_ILLEGAL(217, "请求不合法"),
    SECKILL_ORDER_SUCCESS(218, "下单成功"),

    /**
     * 优惠券
     */
    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕")
    ;

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
