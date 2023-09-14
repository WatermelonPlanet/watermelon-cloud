package com.watermelon.user.api;

import com.watermelon.user.api.dto.SysRegisteredClientDto;
import com.watermelon.user.api.vo.SysRegisteredClientDetailVo;

/**
 * 客户端操作 api
 * @author byh
 * @date 2023-09-14
 * @description
 */
public interface SysRegisteredClientApi {

    /**
     * clientId 查询客户端信息
     * @param clientId
     * @return
     */
    SysRegisteredClientDetailVo getOneByClientId(String clientId);

    /**
     * 保存client
     * @param clientDto
     * @return
     */
    String saveClient(SysRegisteredClientDto clientDto);

    /**
     * id 查询客户端信息
     * @param id
     * @return
     */
    SysRegisteredClientDetailVo getOneById(String id);

}
