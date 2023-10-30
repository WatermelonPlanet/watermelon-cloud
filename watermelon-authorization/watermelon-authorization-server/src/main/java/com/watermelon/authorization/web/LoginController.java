package com.watermelon.authorization.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author Steve Riesenberg
 * @since 1.1
 */
@Controller
public class LoginController {

	@GetMapping("/sso-login")
	public String ssoLogin() {
		return "sso-login";
	}

}
