package com.watermelon.tenant.user.api.feignclients;

import com.watermelon.common.core.util.R;
import com.watermelon.tenant.user.api.vo.SysUserDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@FeignClient(contextId = "sysUserFeignClient",value = "watermelon-tenant-user-server")
public interface SysUserFeignClient {


    @PostMapping("/sys_user/find_one_by_phone")
    R<SysUserDetailVo> findOneByPhone(@RequestBody String phone);

}
