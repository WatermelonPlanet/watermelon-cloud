package com.watermelon.user.api;

import com.watermelon.user.api.vo.SysUserDetailVo;

/**
 * 用户操作 api
 * @author byh
 * @date 2023-09-14
 * @description
 */
public interface SysUserClientApi {

    /**
     * 通过手机号查询用户基础信息
     * @param phone
     * @return
     */
    SysUserDetailVo findOneByPhone(String phone);
}
