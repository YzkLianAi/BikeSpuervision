package com.computer.bikeSupervision.interceprot;

import com.alibaba.fastjson.JSONObject;

import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@CrossOrigin
@Component //交给IOC容器管理 用于后续配置了当中的bean注入
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求的url：{}", url);

        //3.获取请求头中的令牌（token）
        String newJwt = request.getHeader("token");
        try {
            //如果前端没有传递过来token则该数组为空 为报出空异常
            String[] split = newJwt.split(" ");
            String jwt = split[1];
        } catch (Exception e) {
            e.printStackTrace();
            log.info("未携带token");
            Result<String> error = Result.error("未携带token");
            //手动转换 将对象 -> json ——————>利用阿里巴巴fastJson工具类
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);//将错误信息返回给前端
            return false;
        }

        //如果上面能走通则说明能获取到token 下面就是解析token是否正确
        String[] split = newJwt.split(" ");
        String jwt = split[1];
        log.info("处理好后的令牌信息为：{}", jwt);

        //4.判断令牌是否存在 如果不存在 返回一个错误结果（未登录）
        if (!StringUtils.hasLength(jwt)) { //如果 jwt为null 那么取反的结果就是true
            log.info("请求头token为空，返回未登录信息");
            Result<String> error = Result.error("NOT_LOGIN"); //此处是和前端约定的一个信息 若前端接受到该信息 就返回到登录界面 重新登录
            //手动转换 将对象 -> json ——————>利用阿里巴巴fastJson工具类
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);//将错误信息返回给前端
            return false;
        }

        //5.解析token 如果解析失败 返回错误信息（未登录）
        try {
            JwtUtils.parseJWT(jwt);  //快捷键执行包围操作：Ctrl + Art + T
        } catch (Exception exception) {
            exception.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            Result<String> error = Result.error("令牌解析失败");
            //手动转换 将对象 -> json ——————>利用阿里巴巴fastJson工具类
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);//将错误信息返回给前端
            return false;
        }

        log.info("令牌合法，放行");
        //6.解析jwt令牌中的id属性 并强转为Integer类型 表示用户i d
        Claims claims = JwtUtils.parseJWT(jwt);
        //这其实是一个很怪的问题 明明在封装的时候是用的long类型 ->会根据当前的id值在封装claims自动选择合适的类型
        Object id = claims.get("id");

        long Id; // 先初始化Id为一个默认值，以便在后续处理中使用

        //当前的id是 Long 类型
        if (id instanceof Long) {
            Id = (long) id;
        } else { //当前id 是 Integer类型
            Id = ((Integer) id).longValue();
        }

        BaseContext.setCurrentId(Id);//设置当前线程id

        //7.放行
        return true;
    }
}
