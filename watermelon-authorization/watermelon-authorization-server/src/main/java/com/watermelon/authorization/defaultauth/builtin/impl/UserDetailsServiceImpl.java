package com.watermelon.authorization.defaultauth.builtin.impl;

import com.watermelon.authorization.defaultauth.builtin.dto.SysUserDto;
import com.watermelon.authorization.user.core.dto.SysUserDetailDto;
import com.watermelon.authorization.user.core.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @author byh
 * @date 2023-09-15 16:23
 * @description
 */
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //如今这个世界 我们肯定都用手机号登录的了
        SysUserDetailDto sysUser = sysUserService.findOneByPhone(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("手机号错误或不存在!");
        }
        //todo 后续可自行修改和完善
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("/oauth2/token", "/oauth2/authorize","/authorized");
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setUsername(username);
        sysUserDto.setAuthorities(authorityList);
        sysUserDto.setId(sysUser.getId());
        sysUserDto.setAvatar(sysUser.getAvatar());
        sysUserDto.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserDto.setStatus(sysUser.getStatus());
        sysUserDto.setPhone(sysUser.getPhone());
        return sysUserDto;
    }
}
