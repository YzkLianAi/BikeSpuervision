package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.mapper.AdministratorMapper;
import com.computer.bikeSupervision.pojo.dto.AdministratorLoginDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class AdministratorServiceImpl extends ServiceImpl<AdministratorMapper, Administrator>
        implements AdministratorService {

    /**
     *登录
     */
    @Override
    public String login(AdministratorLoginDto administratorLoginDto) {
        // 创建条件构造器
        LambdaQueryWrapper<Administrator> queryWrapper = new LambdaQueryWrapper<>();
        // 比对 账号 密码 和 学校 是否对应
        queryWrapper.eq(Administrator::getAdminNumber, administratorLoginDto.getAdminNumber())
                .eq(Administrator::getPassword, administratorLoginDto.getPassword());
                //.eq(Administrator::getSchoolName, administratorLoginDto.getSchoolName());

        Administrator administrator = this.getOne(queryWrapper);
        if (administrator != null) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("id", administrator.getId());
            claims.put("name", administrator.getAdminName());
            claims.put("number", administrator.getAdminNumber());

            String jwt = JwtUtils.generateJwt(claims);
            String token = "Bearea" + " " + jwt;
            log.info(token);
            //将生成的令牌返回 后续前端的每次请求都必须携带这个令牌
            return token;
        }
        throw new CustomException("用户名或密码错误");

    }

    @Override
    public void register(Administrator administrator) {
        //需要重新设置一下createUser 和 updateUser
        //设置当前线程id 用于后续公共字段填充
        BaseContext.setCurrentId(administrator.getId());
        //自己是自己的创建人
        administrator.setCreateUser(administrator.getId());

        log.info("新注册的学生信息:{}", administrator);

        //创建时间在新增的时候就已经提供了 此处只会修改 修改时间 和 修改人
        this.updateById(administrator);
    }

    @Override
    public void update(Administrator administrator) {
        // 需要先比对密码是否经过修改 -> 如果密码是将 admin123 重新加密后的结果 则说明密码没有经过修改不需要再经过md5加密
        // 将其重新设置成 admin123 经过md5加密后的结果
        if (administrator.getPassword().equals(DigestUtils.md5DigestAsHex("admin123".getBytes()))) {
            administrator.setPassword(DigestUtils.md5DigestAsHex("admin123".getBytes()));
        } else {
            administrator.setPassword(DigestUtils.md5DigestAsHex(administrator.getPassword().getBytes()));
        }

        this.updateById(administrator);
    }
}




