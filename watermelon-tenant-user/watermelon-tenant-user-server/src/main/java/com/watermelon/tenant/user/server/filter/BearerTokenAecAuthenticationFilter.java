
package com.watermelon.tenant.user.server.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class BearerTokenAecAuthenticationFilter extends OncePerRequestFilter {

	private  AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

	private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	private AuthenticationEntryPoint authenticationEntryPoint = new BearerTokenAuthenticationEntryPoint();

	private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationEntryPointFailureHandler(
			(request, response, exception) -> this.authenticationEntryPoint.commence(request, response, exception));

	private BearerTokenResolver bearerTokenResolver = new AecBearerTokenResolver();

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

	private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

	public BearerTokenAecAuthenticationFilter() {
	}


	/**
	 * Extract any
	 * <a href="https://tools.ietf.org/html/rfc6750#section-1.2" target="_blank">Bearer
	 * Token</a> from the request and attempt an authentication.
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token;
		try {
			token = this.bearerTokenResolver.resolve(request);
		}
		catch (OAuth2AuthenticationException invalid) {
			this.logger.trace("Sending to authentication entry point since failed to resolve bearer token", invalid);
			this.authenticationEntryPoint.commence(request, response, invalid);
			return;
		}
		if (token == null) {
			this.logger.trace("Did not process request since did not find bearer token");
			filterChain.doFilter(request, response);
			return;
		}

		BearerTokenAecAuthenticationToken authenticationRequest = new BearerTokenAecAuthenticationToken(token);
		authenticationRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

		try {
			AuthenticationManager authenticationManager = this.authenticationManagerResolver.resolve(request);
			Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
			SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
			context.setAuthentication(authenticationResult);
			this.securityContextHolderStrategy.setContext(context);
			this.securityContextRepository.saveContext(context, request, response);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authenticationResult));
			}
			filterChain.doFilter(request, response);
		}
		catch (AuthenticationException failed) {
			this.securityContextHolderStrategy.clearContext();
			this.logger.trace("Failed to process authentication request", failed);
			this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
		}
	}


	/**
	 * Construct a {@code BearerTokenAuthenticationFilter} using the provided parameter(s)
	 * @param authenticationManager
	 */
	public void setAuthenticationManagerResolver(AuthenticationManager authenticationManager) {
		Assert.notNull(authenticationManager, "authenticationManager cannot be null");
		this.authenticationManagerResolver = (request) -> authenticationManager;
	}

	/**
	 * Sets the {@link SecurityContextHolderStrategy} to use. The default action is to use
	 * the {@link SecurityContextHolderStrategy} stored in {@link SecurityContextHolder}.
	 *
	 * @since 5.8
	 */
	public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
		Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
		this.securityContextHolderStrategy = securityContextHolderStrategy;
	}

	/**
	 * Sets the {@link SecurityContextRepository} to save the {@link SecurityContext} on
	 * authentication success. The default action is not to save the
	 * {@link SecurityContext}.
	 * @param securityContextRepository the {@link SecurityContextRepository} to use.
	 * Cannot be null.
	 */
	public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
		Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
		this.securityContextRepository = securityContextRepository;
	}

	/**
	 * Set the {@link BearerTokenResolver} to use. Defaults to
	 * {@link DefaultBearerTokenResolver}.
	 * @param bearerTokenResolver the {@code BearerTokenResolver} to use
	 */
	public void setBearerTokenResolver(BearerTokenResolver bearerTokenResolver) {
		Assert.notNull(bearerTokenResolver, "bearerTokenResolver cannot be null");
		this.bearerTokenResolver = bearerTokenResolver;
	}

	/**
	 * Set the {@link AuthenticationEntryPoint} to use. Defaults to
	 * {@link BearerTokenAuthenticationEntryPoint}.
	 * @param authenticationEntryPoint the {@code AuthenticationEntryPoint} to use
	 */
	public void setAuthenticationEntryPoint(final AuthenticationEntryPoint authenticationEntryPoint) {
		Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint cannot be null");
		this.authenticationEntryPoint = authenticationEntryPoint;
	}





}
