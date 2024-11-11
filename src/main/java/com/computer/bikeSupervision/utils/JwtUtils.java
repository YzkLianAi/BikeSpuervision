package com.computer.bikeSupervision.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String signKey = "human"; //令牌加密方式
    private static final Long expire = 43200000L; //设置令牌时间

    /**
     * 生成JWT令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return
     */
    public static String generateJwt(Map<String, Object> claims){
        //System.out.println(jwt);
        return Jwts.builder()
                .addClaims(claims)  //令牌内的个人信息内容
                .signWith(SignatureAlgorithm.HS256, signKey) //令牌的加密解密方式
                .setExpiration(new Date(System.currentTimeMillis() + expire)) //令牌的过期时间
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param jwt JWT令牌
     * @return JWT第二部分负载 payload 中存储的内容
     */
    public static Claims parseJWT(String jwt){
        return Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
