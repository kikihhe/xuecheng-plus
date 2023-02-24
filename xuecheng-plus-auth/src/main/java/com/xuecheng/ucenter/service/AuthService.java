package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-24 19:48
 */
public interface AuthService {
    public XcUserExt execute(AuthParamsDto authParamsDto);
}
