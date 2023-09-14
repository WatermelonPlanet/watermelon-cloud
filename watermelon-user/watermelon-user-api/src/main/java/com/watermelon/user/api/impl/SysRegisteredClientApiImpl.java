package com.watermelon.user.api.impl;

import com.watermelon.user.api.SysRegisteredClientApi;
import com.watermelon.user.api.dto.SysRegisteredClientDto;
import com.watermelon.user.api.feignclients.SysRegisteredClientFeignClient;
import com.watermelon.user.api.vo.SysRegisteredClientDetailVo;
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
