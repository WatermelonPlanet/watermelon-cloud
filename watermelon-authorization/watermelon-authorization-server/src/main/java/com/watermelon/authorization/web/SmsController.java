package com.watermelon.authorization.web;

import com.watermelon.authorization.SmsCodeService;
import com.watermelon.common.core.util.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Steve Riesenberg
 * @since 1.1
 */
@RestController
public class SmsController {


    @Resource
    public SmsCodeService smsCodeService;

    @GetMapping("/sms_code")
    public R<String> sentSmsCode(@RequestParam("phone") String phone) {
        return R.ok(smsCodeService.sentValidCode(phone));
    }
}
