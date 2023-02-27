package com.xuecheng.ucenter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-23 20:45
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    // 密码模式登录
    private PasswordAuthServiceImpl passwordAuthService;

    private WXAuthServiceImpl wxAuthService;

    @Autowired
    public UserDetailsServiceImpl(PasswordAuthServiceImpl passwordAuthService, WXAuthServiceImpl wxAuthService) {
        this.passwordAuthService = passwordAuthService;
        this.wxAuthService = wxAuthService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthParamsDto authParamsDto = null;
        try {
            authParamsDto = objectMapper.readValue(s, AuthParamsDto.class);
            if (Objects.isNull(authParamsDto)) {
                throw new RuntimeException("输入信息格式错误");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String authType = authParamsDto.getAuthType();
        UserDetails userDetails = null;
        if ("password".equals(authType)) {
            XcUserExt xcUser = passwordAuthService.execute(authParamsDto);
            // 如果从数据库中查到了用户，拿到密码交给security
            userDetails = creatUserDetails(xcUser);

        } else if ("wx".equals(authType)) {
            XcUserExt execute = wxAuthService.execute(authParamsDto);
            userDetails = creatUserDetails(execute);
        }


        return userDetails;
    }

    private UserDetails creatUserDetails(XcUserExt xcUser) {
        String[] authorities = {"test"};
        xcUser.setPassword(null);
        String userJson = null;
        try {
            userJson = new ObjectMapper().writeValueAsString(xcUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 将用户的信息以json的格式存入username, 以后用的时候解析即可。
        return User.withUsername(userJson).password("").authorities(authorities).build();
    }

}
