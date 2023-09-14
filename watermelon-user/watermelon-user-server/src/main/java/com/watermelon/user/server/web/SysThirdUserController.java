package com.watermelon.user.server.web;

import com.watermelon.common.core.util.R;
import com.watermelon.user.server.dto.SysThirdUserDetailDto;
import com.watermelon.user.server.service.SysThirdUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 第三方用户controller
 *
 * @author byh
 * @date 2023-09-14
 * @description
 */
@RestController
@RequestMapping("/sys_third_user")
@RequiredArgsConstructor
public class SysThirdUserController {

    private final SysThirdUserService sysThirdUserService;


    @GetMapping("/get_one_by_unique_id/{uniqueId}")
    public R<SysThirdUserDetailDto> getOneByUniqueId(@PathVariable("uniqueId") String uniqueId) {
        return R.ok(sysThirdUserService.getOneByUniqueId(uniqueId));
    }

    @GetMapping("/get_one_by_user_id/{userId}")
    public R<SysThirdUserDetailDto> getOneByUserId(@PathVariable("userId") String userId) {
        return R.ok(sysThirdUserService.getOneByUserId(userId));
    }

    @PostMapping("/submit")
    public R<Long> submit(@RequestBody SysThirdUserDetailDto sysThirdUserDetailDto) {
        return R.ok(sysThirdUserService.submit(sysThirdUserDetailDto));
    }
}
