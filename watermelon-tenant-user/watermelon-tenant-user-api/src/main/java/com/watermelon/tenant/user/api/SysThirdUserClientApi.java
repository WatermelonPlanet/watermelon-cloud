package com.watermelon.tenant.user.api;

import com.watermelon.tenant.user.api.dto.SysThirdUserDetailDto;
import com.watermelon.tenant.user.api.vo.SysThirdUserDetailVo;

/**
 * 第三方用户操作 api
 *
 * @author byh
 * @date 2023-09-14
 * @description
 */
public interface SysThirdUserClientApi {

    /**
     * 查询第三方用户基础信息
     *
     * @param uniqueId 第三方用户 唯一id
     * @return
     */
    SysThirdUserDetailVo getOneByUniqueId(String uniqueId);

    /**
     * 查询第三方用户基础信息
     *
     * @param userId 系统用户 唯一id
     * @return
     */
    SysThirdUserDetailVo getOneByUserId(String userId);

    /**
     * 新增|更新
     *
     * @param sysThirdUserDetailDto
     * @return
     */
    Long submit(SysThirdUserDetailDto sysThirdUserDetailDto);


}
