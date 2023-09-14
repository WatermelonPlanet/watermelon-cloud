package com.watermelon.user.api.impl;

import com.watermelon.user.api.SysThirdUserClientApi;
import com.watermelon.user.api.dto.SysThirdUserDetailDto;
import com.watermelon.user.api.feignclients.SysThirdUserFeignClient;
import com.watermelon.user.api.vo.SysThirdUserDetailVo;
import lombok.RequiredArgsConstructor;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@RequiredArgsConstructor
public class SysThirdUserClientApiImpl implements SysThirdUserClientApi {

    private final SysThirdUserFeignClient sysThirdUserFeignClient;

    @Override
    public SysThirdUserDetailVo getOneByUniqueId(String uniqueId) {
        return sysThirdUserFeignClient.getOneByUniqueId(uniqueId).getData();
    }

    @Override
    public SysThirdUserDetailVo getOneByUserId(String userId) {
        return sysThirdUserFeignClient.getOneByUserId(userId).getData();
    }

    @Override
    public Long submit(SysThirdUserDetailDto sysThirdUserDetailDto) {
        return sysThirdUserFeignClient.submit(sysThirdUserDetailDto).getData();
    }
}
