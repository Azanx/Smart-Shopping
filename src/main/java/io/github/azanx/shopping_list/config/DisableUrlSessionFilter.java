/**
 * 
 */
package io.github.azanx.shopping_list.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * disable encoding of session ID in the URL 
 * @author Kamil Piwowarski
 *
 */
public class DisableUrlSessionFilter implements Filter {

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		if(servletRequest instanceof HttpServletRequest) {
			servletResponse= new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {
				public String encodeRedirectURL(String url) {return url;}
				public String encodeURL(String url) {return url;}
			};
		}
		
		filterChain.doFilter(servletRequest, servletResponse);
	}

}
