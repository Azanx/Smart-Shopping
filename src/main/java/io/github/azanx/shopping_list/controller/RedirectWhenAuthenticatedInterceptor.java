/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Redirect to the home page ("/") if user is authenticated, when registering should be further configured by addPathPatterns
 * @author Kamil Piwowarski
 *
 */
public class RedirectWhenAuthenticatedInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAuthenticated;
		if (authentication != null) {
			isAuthenticated = authentication instanceof AnonymousAuthenticationToken ? false
					: authentication.isAuthenticated();
			if (isAuthenticated) {
				response.setContentType("text/plain");
				sendRedirect(request, response);
				return false; // no need to proceed with the chain as we already dealt with the response
			}
		}
		return true;
	}

	private void sendRedirect(HttpServletRequest request, HttpServletResponse response) {

		String encodedRedirectURL = response.encodeRedirectURL(request.getContextPath() + "/");
		response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
		response.setHeader("Location", encodedRedirectURL);
	}

}
