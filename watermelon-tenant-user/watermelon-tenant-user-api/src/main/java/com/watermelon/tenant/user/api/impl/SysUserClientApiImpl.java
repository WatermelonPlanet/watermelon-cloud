package com.watermelon.tenant.user.api.impl;

import com.watermelon.tenant.user.api.SysUserClientApi;
import com.watermelon.tenant.user.api.feignclients.SysUserFeignClient;
import com.watermelon.tenant.user.api.vo.SysUserDetailVo;
import lombok.RequiredArgsConstructor;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@RequiredArgsConstructor
public class SysUserClientApiImpl implements SysUserClientApi {

    private final SysUserFeignClient sysUserFeignClient;


    @Override
    public SysUserDetailVo findOneByPhone(String phone) {
        return sysUserFeignClient.findOneByPhone(phone).getData();
    }
}
