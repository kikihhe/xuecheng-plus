package com.xuecheng.ucenter.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-26 19:30
 */
@Service
@Slf4j
@Transactional
public class WXAuthServiceImpl implements AuthService {

    @Value("${weixin.appid}")
    String appid;
    @Value("${weixin.secret}")
    String secret;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private XcUserMapper userMapper;
    @Autowired
    private XcUserRoleMapper xcUserRoleMapper;


    private static final ObjectMapper objectMapper = new ObjectMapper();



    /**
     * 获取用户信息的接口，由wxLogin调用，获取信息后wxLogin转发,如果获取到用户信息，转发到登陆界面。如果获取为空，转发到错误页面
     *  拿授权码申请令牌，查询用户
     * @param code 授权码
     * @return
     */
    public XcUser wxAuth(String code) throws JsonProcessingException {
        // 获取access_token
        String url_getAccessToken = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> exchange = restTemplate.exchange(url_getAccessToken, HttpMethod.POST, null, String.class);
        String body = exchange.getBody();
        // 拿到map，map中含有access_token
        Map map1 = objectMapper.readValue(body, Map.class);
        // 根据access_token拿到用户信息
        Object access_token = map1.get("access_token");
        Object openid = map1.get("openid");
        String url_getUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
        url_getUserInfo = String.format(url_getUserInfo, access_token, openid);
        ResponseEntity<String> exchange1 = restTemplate.exchange(url_getUserInfo, HttpMethod.GET, null, String.class);
        // 拿到map, map中有用户信息
        Map map = objectMapper.readValue(exchange1.getBody(), Map.class);
        String unionid = (String) map.get("unionid");
        LambdaQueryWrapper<XcUser> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(XcUser::getWxUnionid, unionid);
        XcUser xcUser = userMapper.selectOne(wrapper1);
        // 如果数据库中存在对应wxunionid的用户，说明已经登陆过，不是第一次登录。可以直接返回
        if (!Objects.isNull(xcUser)) {
            return xcUser;
        }
        // 如果不存在，说明没登录过，插入
        xcUser = new XcUser();
        xcUser.setId(unionid);
        xcUser.setWxUnionid(unionid);
        xcUser.setNickname((String)map.get("nickname"));
        xcUser.setName((String)map.get("nickname"));
        xcUser.setUserpic((String)map.get("headimgurl"));
        xcUser.setUsername(unionid);
        // 学生类型
        xcUser.setUtype("101001");
        xcUser.setStatus("1");
        xcUser.setCreateTime(LocalDateTime.now());
        int insert = userMapper.insert(xcUser);
        if (insert <= 0) {
            log.error("微信用户入库失败");
            throw new RuntimeException("登录失败");
        }
        // 插入角色表
        XcUserRole xcUserRole = new XcUserRole();
        xcUserRole.setId(UUID.randomUUID().toString());
        xcUserRole.setUserId(unionid);
        xcUserRole.setRoleId("17"); // 角色: 学生
        int insert1 = xcUserRoleMapper.insert(xcUserRole);
        if (insert1 <= 0) {
            log.error("微信登录用户权限入库失败");
            throw new RuntimeException("登陆失败");
        }
        return xcUser;
    }


    /**
     * 正常登录请求调用的方法
     * @param authParamsDto
     * @return
     */
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        // 如果用户存在，wxlogincontroller会调用根据username的自动登录
        String username = authParamsDto.getUsername();
        LambdaQueryWrapper<XcUser> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(XcUser::getUsername, username);
        XcUser xcUser = userMapper.selectOne(wrapper);
        if (Objects.isNull(xcUser)) {
            throw new RuntimeException("用户不存在!");
        }
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser, xcUserExt);


        return xcUserExt;
    }
}
