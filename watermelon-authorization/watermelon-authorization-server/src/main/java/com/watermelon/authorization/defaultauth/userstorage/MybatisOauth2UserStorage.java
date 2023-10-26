package com.watermelon.authorization.defaultauth.userstorage;

import com.watermelon.authorization.oauth.client.dto.OAuth2ThirdUserDto;
import com.watermelon.authorization.oauth.client.storage.Oauth2UserStorage;
import com.watermelon.authorization.user.core.dto.SysThirdUserAddDto;
import com.watermelon.authorization.user.core.dto.SysUserAddDto;
import com.watermelon.authorization.user.core.service.SysThirdUserService;
import com.watermelon.authorization.user.core.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author byh
 * @date 2023-09-21 11:46
 * @description
 */
@Primary
@Service("mybatisOauth2UserStorage")
@RequiredArgsConstructor
public class MybatisOauth2UserStorage implements Oauth2UserStorage {

    private final SysThirdUserService sysThirdUserService;

    private final SysUserService sysUserService;

    @Override
    public void save(OAuth2ThirdUserDto auth2ThirdUserDto) {
        SysUserAddDto sysUserAddDto = new SysUserAddDto();
        sysUserAddDto.setName(auth2ThirdUserDto.getName());
        sysUserAddDto.setAvatar(auth2ThirdUserDto.getAvatar());
        sysUserAddDto.setStatus(1);
        Long sysUserId = sysUserService.save(sysUserAddDto);
        SysThirdUserAddDto sysThirdUserAddDto = new SysThirdUserAddDto();
        sysThirdUserAddDto.setUniqueId(auth2ThirdUserDto.getUniqueId());
        sysThirdUserAddDto.setAvatar(auth2ThirdUserDto.getAvatar());
        sysThirdUserAddDto.setPlatform(auth2ThirdUserDto.getPlatform());
        sysThirdUserAddDto.setName(auth2ThirdUserDto.getName());
        sysThirdUserAddDto.setUserId(sysUserId);
        sysThirdUserService.save(sysThirdUserAddDto);
    }
}
