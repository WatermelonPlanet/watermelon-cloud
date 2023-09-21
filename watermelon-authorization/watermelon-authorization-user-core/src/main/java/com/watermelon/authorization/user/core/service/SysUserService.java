package com.watermelon.authorization.user.core.service;


import com.watermelon.authorization.user.core.dto.SysUserAddDto;
import com.watermelon.authorization.user.core.dto.SysUserDetailDto;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
public interface SysUserService {
    /**
     * 手机号查询用户详情
     *
     * @param phone
     * @return
     */
    SysUserDetailDto findOneByPhone(String phone);

    /**
     * 用户名查询用户详情
     * @param userName
     * @return
     */
    SysUserDetailDto findOneByUserName(String userName);

    /**
     * 新增
     * @param sysUserAddDto
     * @return
     */
    Long save(SysUserAddDto sysUserAddDto);


}
