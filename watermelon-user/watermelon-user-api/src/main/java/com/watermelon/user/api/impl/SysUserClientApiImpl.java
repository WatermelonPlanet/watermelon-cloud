package com.watermelon.user.api.impl;

import com.watermelon.user.api.SysUserClientApi;
import com.watermelon.user.api.feignclients.SysUserFeignClient;
import com.watermelon.user.api.vo.SysUserDetailVo;
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
