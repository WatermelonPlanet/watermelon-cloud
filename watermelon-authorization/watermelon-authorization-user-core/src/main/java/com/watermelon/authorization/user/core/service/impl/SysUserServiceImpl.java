package com.watermelon.authorization.user.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.authorization.user.core.dto.SysUserAddDto;
import com.watermelon.authorization.user.core.dto.SysUserDetailDto;
import com.watermelon.authorization.user.core.mapper.SysUserMapper;
import com.watermelon.authorization.user.core.mapper.entity.SysUserDo;
import com.watermelon.authorization.user.core.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserDo> implements SysUserService {


    @Override
    public SysUserDetailDto findOneByPhone(String phone) {
        LambdaQueryWrapper<SysUserDo> userDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userDoLambdaQueryWrapper.eq(SysUserDo::getPhone, phone);
        SysUserDo sysUserDo = this.getOne(userDoLambdaQueryWrapper);
        if (sysUserDo == null) {
            return null;
        }
        return BeanUtil.copyProperties(sysUserDo, SysUserDetailDto.class);
    }

    @Override
    public SysUserDetailDto findOneByUserName(String userName) {
        LambdaQueryWrapper<SysUserDo> userDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userDoLambdaQueryWrapper.eq(SysUserDo::getName, userName);
        SysUserDo sysUserDo = this.getOne(userDoLambdaQueryWrapper);
        if (sysUserDo == null) {
            return null;
        }
        return BeanUtil.copyProperties(sysUserDo, SysUserDetailDto.class);
    }

    @Override
    public Long save(SysUserAddDto sysUserAddDto) {
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setAvatar(sysUserAddDto.getAvatar());
        sysUserDo.setName(sysUserAddDto.getName());
        sysUserDo.setPassword("123456");
        sysUserDo.setStatus(1);
        sysUserDo.setPhone(sysUserAddDto.getPhone());
        sysUserDo.setMobile(sysUserAddDto.getPhone());
        if (StringUtils.hasText(sysUserDo.getPhone())) {
            LambdaQueryWrapper<SysUserDo> userDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userDoLambdaQueryWrapper.eq(SysUserDo::getPhone, sysUserDo.getPhone());
            SysUserDo sysUserExist = this.getOne(userDoLambdaQueryWrapper);
            if (sysUserExist != null) {
                return sysUserExist.getId();
            }
        }
        this.save(sysUserDo);
        return sysUserDo.getId();
    }
}
