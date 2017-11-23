package io.github.azanx.shopping_list.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
@ComponentScan("io.github.azanx.shopping_list")
@EnableWebMvc
/*
 * @EnableSpringDataWebSupport // registers a DomainClassConverter to enable
 * Spring // MVC to resolve instances of repository managed // domain classes
 * from request parameters or path // variables. and
 * andlerMethodArgumentResolver // implementations to let Spring MVC resolve //
 * Pageable and Sort instances from request // parameters.
 */
public class WebMvcConfig extends WebMvcConfigurerAdapter {

//	@Bean
//	public DataSource dataSource() {
//		final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
//		dsLookup.setResourceRef(true);
//		DataSource dataSource = dsLookup.getDataSource("jdbc/springdb");
//		return dataSource;
//	}

	@Bean
	public UrlBasedViewResolver urlBasedViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("/index");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/css/");
	}
}
