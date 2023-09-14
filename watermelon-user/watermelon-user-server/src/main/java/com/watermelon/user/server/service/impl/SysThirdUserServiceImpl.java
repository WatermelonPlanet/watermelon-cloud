package com.watermelon.user.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.user.server.dto.SysThirdUserDetailDto;
import com.watermelon.user.server.mapper.SysThirdUserMapper;
import com.watermelon.user.server.mapper.entity.SysThirdUserDo;
import com.watermelon.user.server.service.SysThirdUserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 第三方服务实现类
 *
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Service
public class SysThirdUserServiceImpl extends ServiceImpl<SysThirdUserMapper, SysThirdUserDo> implements SysThirdUserService {


    @Override
    public SysThirdUserDetailDto getOneByUniqueId(String uniqueId) {
        SysThirdUserDo sysThirdUserDo = this.getOne(new LambdaQueryWrapper<SysThirdUserDo>()
                .eq(SysThirdUserDo::getUniqueId, uniqueId));
        return Optional.ofNullable(sysThirdUserDo)
                .map(user -> BeanUtil.copyProperties(user, SysThirdUserDetailDto.class))
                .orElse(null);
    }

    @Override
    public SysThirdUserDetailDto getOneByUserId(String userId) {
        SysThirdUserDo sysThirdUserDo = this.getOne(new LambdaQueryWrapper<SysThirdUserDo>()
                .eq(SysThirdUserDo::getUserId, userId));
        return Optional.ofNullable(sysThirdUserDo)
                .map(user -> BeanUtil.copyProperties(user, SysThirdUserDetailDto.class))
                .orElse(null);
    }

    @Override
    public Long submit(SysThirdUserDetailDto sysThirdUser) {
        SysThirdUserDo sysThirdUserDo = this.getOne(new LambdaQueryWrapper<SysThirdUserDo>()
                .eq(SysThirdUserDo::getUniqueId, sysThirdUser.getUniqueId()));

        if (sysThirdUserDo == null) {
            sysThirdUserDo = BeanUtil.copyProperties(sysThirdUser, SysThirdUserDo.class);
            this.save(sysThirdUserDo);
        }
        return sysThirdUserDo.getId();
    }
}
