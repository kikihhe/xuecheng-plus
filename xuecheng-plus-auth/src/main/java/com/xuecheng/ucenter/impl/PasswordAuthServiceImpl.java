package com.xuecheng.ucenter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-24 19:48
 */
@Service
public class PasswordAuthServiceImpl implements AuthService {
    private XcUserMapper xcUserMapper;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public PasswordAuthServiceImpl(XcUserMapper xcUserMapper, PasswordEncoder passwordEncoder) {
        this.xcUserMapper = xcUserMapper;
        this.passwordEncoder = passwordEncoder;
    }



    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        LambdaQueryWrapper<XcUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(XcUser::getUsername, authParamsDto.getUsername());

        XcUser xcUser = xcUserMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(xcUser)) {
            throw new UsernameNotFoundException("账号或密码错误!");
        }
        String inputPassword = authParamsDto.getPassword();
        String truePassword = xcUser.getPassword();
        boolean matches = passwordEncoder.matches(inputPassword, truePassword);

        if (!matches) {
            throw new UsernameNotFoundException("账号或密码错误!");
        }
        // 到这里就成功认证
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        return xcUserExt;

    }

}
