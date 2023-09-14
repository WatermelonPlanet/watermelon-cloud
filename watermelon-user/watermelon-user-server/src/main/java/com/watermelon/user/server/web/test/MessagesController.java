package com.watermelon.user.server.web.test;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RestController
public class MessagesController {

	@GetMapping("/messages")
	public String[] getMessages() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}
}
