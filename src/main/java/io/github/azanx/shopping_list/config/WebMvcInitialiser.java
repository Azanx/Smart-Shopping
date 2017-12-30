package io.github.azanx.shopping_list.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import io.github.azanx.shopping_list.config.security.WebSecurityConfig;

/**
 * Configuration class for ServletContext
 * @author Kamil Piwowarski
 *
 */
@Order(1)
public class WebMvcInitialiser implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WebMvcConfig.class);
		rootContext.register(JPAConfig.class);
		rootContext.register(WebSecurityConfig.class);
		rootContext.register(ServiceConfig.class);
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(rootContext));
		dispatcher.addMapping("/");
		dispatcher.setLoadOnStartup(1);
		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("endcodingFilter", new CharacterEncodingFilter());
		filterRegistration.setInitParameter("encoding", "UTF-8");
		filterRegistration.setInitParameter("forceEncoding", "true");
		//make sure encodingFilter is matched first
		filterRegistration.addMappingForUrlPatterns(null, false, "/*");
		//disable appending jsessionid to the URL
		filterRegistration = servletContext.addFilter("disableUrlSessionFilter", new DisableUrlSessionFilter());
		filterRegistration.addMappingForUrlPatterns(null, true, "/*");
	}
}
;