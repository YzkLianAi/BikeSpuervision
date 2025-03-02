package com.computer.bikeSupervision.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户userId
 */
public class BaseContext {
    //创建ThreadLocal对象
    private static final ThreadLocal<String> ThreadLocal = new InheritableThreadLocal<>();

    //设置当前线程的操作人id
    public static void setCurrentId(String id) {
        ThreadLocal.set(id);
    }

    //拿取当前操作线程的操作人id
    public static String getCurrentId() {
        return ThreadLocal.get();
    }

}
