package com.watermelon.tenant.user.api.feignclients;

import com.watermelon.common.core.util.R;
import com.watermelon.tenant.user.api.dto.SysRegisteredClientDto;
import com.watermelon.tenant.user.api.vo.SysRegisteredClientDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@FeignClient(contextId = "sysRegisteredClientFeignClient", value = "watermelon-tenant-user-server")
public interface SysRegisteredClientFeignClient {

    @PostMapping("/sys_registered_client/get_one_by_client_id")
    R<SysRegisteredClientDetailVo> getOneByClientId(@RequestBody String clientId);


    @PostMapping("/sys_registered_client/save_client")
    R<String> saveClient(@RequestBody SysRegisteredClientDto clientDto);


    @PostMapping("/sys_registered_client/get_one_by_id/{id}")
    R<SysRegisteredClientDetailVo> getOneById(@PathVariable("id") String id);
}
