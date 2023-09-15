package com.watermelon.tenant.user.api.impl;

import com.watermelon.tenant.user.api.SysRegisteredClientApi;
import com.watermelon.tenant.user.api.dto.SysRegisteredClientDto;
import com.watermelon.tenant.user.api.feignclients.SysRegisteredClientFeignClient;
import com.watermelon.tenant.user.api.vo.SysRegisteredClientDetailVo;
import lombok.RequiredArgsConstructor;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@RequiredArgsConstructor
public class SysRegisteredClientApiImpl implements SysRegisteredClientApi {

    private final SysRegisteredClientFeignClient sysRegisteredClientFeignClient;

    @Override
    public SysRegisteredClientDetailVo getOneByClientId(String clientId) {
        return sysRegisteredClientFeignClient.getOneByClientId(clientId).getData();
    }

    @Override
    public String saveClient(SysRegisteredClientDto clientDto) {
        return sysRegisteredClientFeignClient.saveClient(clientDto).getData();
    }

    @Override
    public SysRegisteredClientDetailVo getOneById(String id) {
        return sysRegisteredClientFeignClient.getOneById(id).getData();
    }
}
