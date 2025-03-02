package com.computer.bikeSupervision.aop;

import com.alibaba.fastjson.JSONObject;

import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.mapper.OperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect //切面类
@Slf4j
//Aop事务管理
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.computer.bikeSupervision.anno.log)")//切入点表达式
    public Object recordLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //操作人
        String operateUser = BaseContext.getCurrentId();

        //操作时间
        LocalDateTime operateTime = LocalDateTime.now();

        //操作类的类名
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //操作类的方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        //操作方法的参数
        Object[] args = proceedingJoinPoint.getArgs();
        String methodParams = Arrays.toString(args);

        //调用原始目标方法运行
        long begin = System.currentTimeMillis();
        //拿到方法运行的结果
        Object result = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();

        //方法执行的返回值 (利用fastJSON工具包中的方法 将result对象 转化为 json格式的字符串)
        String returnValue = JSONObject.toJSONString(result);

        //方法的操作耗时
        long costTime = end - begin;

        //记录操作日志
        //OperateLog operateLog = new OperateLog(null, operateUser, operateTime, className, methodName, methodParams, returnValue, costTime);

        //将日志保存到数据库中
       //operateLogMapper.insert(operateLog);
        log.info("耗时时间：{} ms", costTime);

        return result;
    }
}
