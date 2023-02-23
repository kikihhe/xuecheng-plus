package com.xuecheng.ucenter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.po.XcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-23 20:45
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private XcUserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        LambdaQueryWrapper<XcUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(XcUser::getUsername, s);

        XcUser xcUser = userMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(xcUser)) {
            throw new UsernameNotFoundException("账号/密码错误!");
        }

        // 如果从数据库中查到了用户，拿到密码交给security
        String password = xcUser.getPassword();
        String[] authorities = {"test"};
        xcUser.setPassword(null);
        String userJson = null;
        try {
            userJson = new ObjectMapper().writeValueAsString(xcUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 将用户的信息以json的格式存入username, 以后用的时候解析即可。
        return User.withUsername(userJson).password(password).authorities(authorities).build();
    }
}
