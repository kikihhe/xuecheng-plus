package com.xuecheng.ucenter.impl;

import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.feignclient.CheckCodeClient;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-24 19:48
 */
@Service
@Slf4j
public class PasswordAuthServiceImpl implements AuthService {
    private XcUserMapper xcUserMapper;
    private PasswordEncoder passwordEncoder;
    private CheckCodeClient checkCodeClient;


    @Autowired
    public PasswordAuthServiceImpl(XcUserMapper xcUserMapper, PasswordEncoder passwordEncoder, CheckCodeClient checkCodeClient) {
        this.xcUserMapper = xcUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.checkCodeClient = checkCodeClient;
    }



    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 校验验证码
        String codeKey = authParamsDto.getCheckcodekey();
        String code = authParamsDto.getCheckcode();
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(codeKey)) {
            return null;
        }
        Boolean verify = checkCodeClient.verify(codeKey, code);
        // 如果验证为空，证明走了熔断,
        // 如果验证为真或假，证明可以验证
        if (Objects.isNull(verify)) {
            log.error("验证码服务走了熔断降级处理");
            throw new RuntimeException("验证码出错误");
        }
        if (verify.equals(Boolean.FALSE)) {
            throw new RuntimeException("验证码错误!");
        }


        // 查看用户名/密码是否正确
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
            throw new RuntimeException("账号或密码错误!");
        }
        // 到这里就成功认证
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);
        return xcUserExt;

    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("11111"));
    }
}
