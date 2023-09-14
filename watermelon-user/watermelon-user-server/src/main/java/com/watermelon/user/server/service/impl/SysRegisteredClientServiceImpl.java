package com.watermelon.user.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.watermelon.user.server.dto.SysRegisteredClientDto;
import com.watermelon.user.server.mapper.SysRegisteredClientMapper;
import com.watermelon.user.server.mapper.entity.SysRegisteredClientDo;
import com.watermelon.user.server.service.SysRegisteredClientService;
import org.springframework.stereotype.Service;

/**
 * (Oauth2RegisteredClient)表服务实现类
 *
 * @author byh
 * @since 2023-09-14
 */
@Service
public class SysRegisteredClientServiceImpl extends ServiceImpl<SysRegisteredClientMapper, SysRegisteredClientDo> implements SysRegisteredClientService {


    @Override
    public SysRegisteredClientDto getOneByClientId(String clientId) {
        LambdaQueryWrapper<SysRegisteredClientDo> oauth2RegisteredClientDoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        oauth2RegisteredClientDoLambdaQueryWrapper.eq(SysRegisteredClientDo::getClientId, clientId);
        SysRegisteredClientDo sysRegisteredClientDo = this.getOne(oauth2RegisteredClientDoLambdaQueryWrapper);
        if (sysRegisteredClientDo != null) {
            return BeanUtil.copyProperties(sysRegisteredClientDo, SysRegisteredClientDto.class);
        }
        return null;
    }

    @Override
    public String saveClient(SysRegisteredClientDto clientDto) {
        SysRegisteredClientDo sysRegisteredClientDo = BeanUtil.copyProperties(clientDto, SysRegisteredClientDo.class);
        this.save(sysRegisteredClientDo);
        return sysRegisteredClientDo.getId();
    }

    @Override
    public SysRegisteredClientDto getOneById(String id) {
        SysRegisteredClientDo sysRegisteredClientDo = this.getById(id);
        if (sysRegisteredClientDo != null) {
            return BeanUtil.copyProperties(sysRegisteredClientDo, SysRegisteredClientDto.class);
        }
        return null;
    }
}

