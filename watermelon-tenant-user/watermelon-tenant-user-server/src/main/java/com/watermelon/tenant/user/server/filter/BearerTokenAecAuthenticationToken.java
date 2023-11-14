

package com.watermelon.tenant.user.server.filter;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collections;


public class BearerTokenAecAuthenticationToken extends AbstractAuthenticationToken {

	private final String token;

	/**
	 * Create a {@code BearerTokenAuthenticationToken} using the provided parameter(s)
	 * @param token - the bearer token
	 */
	public BearerTokenAecAuthenticationToken(String token) {
		super(Collections.emptyList());
		Assert.hasText(token, "token cannot be empty");
		this.token = token;
	}

	/**
	 * Get the
	 * <a href="https://tools.ietf.org/html/rfc6750#section-1.2" target="_blank">Bearer
	 * Token</a>
	 * @return the token that proves the caller's authority to perform the
	 * {@link jakarta.servlet.http.HttpServletRequest}
	 */
	public String getToken() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.getToken();
	}

	@Override
	public Object getPrincipal() {
		return this.getToken();
	}

}
