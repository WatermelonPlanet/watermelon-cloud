package com.watermelon.user.server.service;


import com.watermelon.user.server.dto.SysRegisteredClientDto;

/**
 * (Oauth2RegisteredClient)表服务接口
 *
 * @author byh
 * @since 2023-09-14
 */
public interface SysRegisteredClientService {

    SysRegisteredClientDto getOneByClientId(String clientId);

    String saveClient(SysRegisteredClientDto clientDto);

    SysRegisteredClientDto getOneById(String id);
}

