package com.watermelon.authorization.web;

import com.watermelon.authorization.SmsCodeService;
import com.watermelon.common.core.util.R;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Steve Riesenberg
 * @since 1.1
 */
@Controller
public class LoginController {


	@Resource
	public SmsCodeService smsCodeService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}


	@GetMapping("/sso-login")
	public String login1() {
		return "sso-login";
	}

	@GetMapping("/get_sms_code")
	@ResponseBody
	public void sentSmsCode(@RequestParam("phone") String phone){
		smsCodeService.sentValidCode(phone);
	}
}
