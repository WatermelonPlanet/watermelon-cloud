package com.watermelon.tenant.user.api.feignclients;

import com.watermelon.common.core.util.R;
import com.watermelon.tenant.user.api.dto.SysThirdUserDetailDto;
import com.watermelon.tenant.user.api.vo.SysThirdUserDetailVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author byh
 * @date 2023-09-14
 * @description
 */
@FeignClient(contextId = "sysThirdUserFeignClient", value = "watermelon-tenant-user-server")
public interface SysThirdUserFeignClient {


    @GetMapping("/sys_third_user/get_one_by_unique_id/{uniqueId}")
    R<SysThirdUserDetailVo> getOneByUniqueId(@PathVariable("uniqueId") String uniqueId);


    @GetMapping("/sys_third_user/get_one_by_user_id/{userId}")
    R<SysThirdUserDetailVo> getOneByUserId(@PathVariable("userId") String userId);


    @PostMapping("/sys_third_user/submit")
    R<Long> submit(@RequestBody SysThirdUserDetailDto sysThirdUserDetailDto);
}
