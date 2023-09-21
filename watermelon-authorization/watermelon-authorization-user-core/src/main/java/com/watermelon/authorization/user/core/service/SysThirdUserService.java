package com.watermelon.authorization.user.core.service;


import com.watermelon.authorization.user.core.dto.SysThirdUserAddDto;
import com.watermelon.authorization.user.core.dto.SysThirdUserDetailDto;

/**
 * 第三方用户服务
 * @author byh
 * @date 2023-09-14
 * @description
 */
public interface SysThirdUserService {
    /**
     * 查询第三方用户数据 by uniqueId
     * @param uniqueId
     * @return
     */
    SysThirdUserDetailDto getOneByUniqueId(String uniqueId);


    /**
     * 查询第三方用户数据 by userId
     * @param userId
     * @return
     */
    SysThirdUserDetailDto getOneByUserId(String userId);

    /**
     * 新增
     * @param sysThirdUserAddDto
     * @return
     */
    Long save(SysThirdUserAddDto sysThirdUserAddDto);
}



