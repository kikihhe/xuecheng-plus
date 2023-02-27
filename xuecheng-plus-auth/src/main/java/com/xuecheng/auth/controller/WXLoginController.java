package com.xuecheng.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xuecheng.ucenter.impl.WXAuthServiceImpl;
import com.xuecheng.ucenter.model.po.XcUser;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * 对接微信登录接口
 */
@Controller
public class WXLoginController {

    private WXAuthServiceImpl wxAuthService;

    @Autowired
    public WXLoginController(WXAuthServiceImpl wxAuthService) {
        this.wxAuthService = wxAuthService;
    }

    @RequestMapping("/wxLogin")
    public String wxLogin(String code, String state) throws JsonProcessingException {
        XcUser xcUser = wxAuthService.wxAuth(code);
        // 如果用户为空,重定向到一个错误页面
        if (Objects.isNull(xcUser)) {
            return "redirect:http://www.xuecheng-plus.com/error.html";
        } else {
            // 重定向到登陆界面，自动登录
            String username = xcUser.getUsername();
//              return "redirect:http://www.xuecheng-plus.com/sign.html?username="+username+"&authType=wx";
            return "redirect:http://www.xuecheng-plus.com/sign.html?username="+username+"&authType=wx";
        }

    }


}
