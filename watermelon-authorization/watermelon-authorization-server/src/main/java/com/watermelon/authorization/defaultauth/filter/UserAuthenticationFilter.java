
package com.watermelon.authorization.defaultauth.filter;

import com.watermelon.authorization.SmsCodeService;
import com.watermelon.authorization.defaultauth.support.phone.PhoneCaptchaAuthenticationToken;
import com.watermelon.common.core.exception.ValidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;


public class UserAuthenticationFilter extends UserAuthenticationProcessingFilter {


	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";

	public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";

	public static final String SPRING_SECURITY_FORM_CODE_KEY = "code";

	private  String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;

	private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

	private final String codeParameter = SPRING_SECURITY_FORM_CODE_KEY;

	private boolean postOnly = true;

	private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/sso-login",
			"POST");

	public UserAuthenticationFilter() {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
	}

	public UserAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
	}

	public SmsCodeService smsCodeService;


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		username = (username != null) ? username.trim() : "";
		String password = obtainPassword(request);
		if(StringUtils.hasText(password)){
			password = (password != null) ? password : "";
			UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
					password);
			// Allow subclasses to set the "details" property
			setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		}else {
			String code = obtainCode(request);
			assert code != null;
			if (!smsCodeService.checkValidCode(username,code)) {
				throw new ValidException("验证码错误或已失效!");
			}
			PhoneCaptchaAuthenticationToken authRequest = PhoneCaptchaAuthenticationToken.unauthenticated(username, code);
			// Allow subclasses to set the "details" property
			this.setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		}

	}


	@Nullable
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(this.passwordParameter);
	}


	@Nullable
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(this.usernameParameter);
	}


	@Nullable
	protected String obtainCode(HttpServletRequest request) {
		return request.getParameter(this.codeParameter);
	}


	protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	protected void setDetails(HttpServletRequest request, PhoneCaptchaAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}



	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}


	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}



	public final String getUsernameParameter() {
		return this.usernameParameter;
	}

	public void setSmsCodeService(SmsCodeService smsCodeService) {
		this.smsCodeService = smsCodeService;
	}

}
