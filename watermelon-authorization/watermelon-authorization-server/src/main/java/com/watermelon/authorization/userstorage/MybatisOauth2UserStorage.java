package com.watermelon.authorization.userstorage;

import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;
import com.watermelon.authorization.oauth.client.storage.Oauth2UserStorage;
import com.watermelon.authorization.user.core.service.SysThirdUserService;
import com.watermelon.authorization.user.core.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author byh
 * @date 2023-09-21 11:46
 * @description
 */
@Service("mybatisOauth2UserStorage")
@RequiredArgsConstructor
public class MybatisOauth2UserStorage implements Oauth2UserStorage {

    private final SysThirdUserService sysThirdUserService;

    private final SysUserService sysUserService;

    @Override
    public void save(OAuth2ThirdUserDto auth2ThirdUserDto) {

//        sysThirdUserService.submit()

    }
}
