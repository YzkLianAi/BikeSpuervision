package com.computer.bikeSupervision.interceprot;

import com.alibaba.fastjson.JSONObject;

import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        //1.获取请求url
        String url = request.getRequestURL().toString();
        log.info("请求的url：{}", url);

        //2.获取请求头中的令牌（token）
        String newJwt = request.getHeader("token");
        //log.info("前端传递过来的token:{}", newJwt);

        //3.检查令牌是否为空
        if (newJwt == null || newJwt.isEmpty()) {
            log.info("未携带token");
            Result<String> error = Result.error("NOT_LOGIN");
            //手动转换 将对象 -> json ——————>利用阿里巴巴fastJson工具类
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);//将错误信息返回给前端
            return false;
        }

        //4.尝试解析令牌
        String[] split;
        String jwt;
        try {
            split = newJwt.split(" ");
            jwt = split[1];
        } catch (Exception e) {
            e.printStackTrace();
            log.info("未携带token");
            Result<String> error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //5.判断令牌是否存在
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，返回未登录信息");
            Result<String> error = Result.error("NOT_LOGIN");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //6.解析token
        Claims claims;
        try {
            claims = JwtUtils.parseJWT(jwt);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.info("解析令牌失败，返回未登录错误信息");
            Result<String> error = Result.error("TOKEN_ERROR");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //7.验证 IP 地址
        String requestIp = request.getRemoteAddr();
        String tokenIp = (String) claims.get("ip");
        if (!requestIp.equals(tokenIp)) {
            log.info("IP 地址不匹配，返回未登录错误信息");
            Result<String> error = Result.error("IP_ERROR");
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        log.info("令牌合法，放行");
        // 解析jwt令牌中的id属性 并强转为Integer类型 表示用户id
        Object id = claims.get("id");

        BaseContext.setCurrentId((String) id);//设置当前线程id

        // 放行
        return true;
    }
}
