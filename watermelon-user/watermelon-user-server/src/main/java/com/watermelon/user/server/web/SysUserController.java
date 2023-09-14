package com.watermelon.user.server.web;


import com.watermelon.common.core.util.R;
import com.watermelon.user.server.dto.SysUserDetailDto;
import com.watermelon.user.server.service.SysUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户查询服务
 * @author byh
 * @date 2023-09-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys_user")
public class SysUserController {

    public final SysUserService userService;

    @PostMapping("/find_one_by_phone")
    public R<SysUserDetailDto> findOneByPhone(@RequestBody String phone){
       return R.ok(userService.findOneByPhone(phone)) ;
    }


    @PostMapping("/user_info")
    public R<SysUserDetailDto> userInfo(){
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        return R.ok(userService.findOneByPhone("user1")) ;
    }
}
