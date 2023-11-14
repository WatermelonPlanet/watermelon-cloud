
package com.watermelon.authorization.oauth2.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class AecEncryptionEndpointFilter extends OncePerRequestFilter {

    private static final String DEFAULT_JWK_SET_ENDPOINT_URI = "/oauth2/aecs";

    private final RequestMatcher requestMatcher;

	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;


    public AecEncryptionEndpointFilter() {
        this(DEFAULT_JWK_SET_ENDPOINT_URI);
    }


    public AecEncryptionEndpointFilter(String aecSetEndpointUri) {
        Assert.hasText(aecSetEndpointUri, "aecSetEndpointUri cannot be empty");
        this.requestMatcher = new AntPathRequestMatcher(aecSetEndpointUri, HttpMethod.GET.name());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!this.requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

		AECSet aecSet;
		try {
			String token = resolveFromAuthorizationHeader(request);
			aecSet = new AECSet(token);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to select the AEC(s) -> " + ex.getMessage(), ex);
		}

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		try (Writer writer = response.getWriter()) {
			writer.write(aecSet.toString());	// toString() excludes private keys
		}
    }

	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
		String authorization = request.getHeader(this.bearerTokenHeaderName);
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		Matcher matcher = authorizationPattern.matcher(authorization);
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		return matcher.group("token");
	}
}
