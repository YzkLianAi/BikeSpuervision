package com.computer.bikeSupervision.common;

import lombok.Data;

import java.util.HashMap;

@Data
public class Result<T> extends HashMap<String, Object> {
    //结果类
    /*private Integer code; //编码：0成功，1和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private String token; //token*/

    public Result() {
    }
    //Result继承了HashMap类 可以使用put方法往里面添加键值对
    //其中包含了三个属性 code message 和 data，分别表示编码、错误信息和数据
    public Result(Integer code, String message, Object data) {
        put("code", code);
        put("message", message);
        put("data", data);
    }

    //private Map map = new HashMap(); //动态数据

    public static <T> Result<T> success(T object) {
        return new Result<>(0, "成功", object);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(1, message, null);
    }

    public static Result<String> setToken(String token) {
        //调用Result 的 无参构造函数
        Result r = new Result();
        r.put("token", token);
        r.put("code", 0);
        r.put("message", "成功");
        return r;
    }
    //这个是用来操作动态数据的
    /*public Result<T> add(String key, Object value) {
        put(key, value);
        return this;
    }*/

}
