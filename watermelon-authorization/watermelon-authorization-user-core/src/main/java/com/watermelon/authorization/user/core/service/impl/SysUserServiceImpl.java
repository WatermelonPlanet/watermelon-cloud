package com.watermelon.authorization.user.core.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.authorization.user.core.dto.SysUserDetailDto;
import com.watermelon.authorization.user.core.mapper.SysUserMapper;
import com.watermelon.authorization.user.core.mapper.entity.SysUserDo;
import com.watermelon.authorization.user.core.service.SysUserService;
import org.springframework.stereotype.Service;

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
}
