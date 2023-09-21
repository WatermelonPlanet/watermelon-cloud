package com.watermelon.authorization.oauth.client.storage;


import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;

/**
 * 第三方平台保存接口定义
 * @author byh
 * @date 2023-09-21 10:38
 * @description
 */
public interface Oauth2UserStorage {

    /**
     * 保存
     * @param auth2ThirdUserDto
     */
    void save(OAuth2ThirdUserDto auth2ThirdUserDto);

}
