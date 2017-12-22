/**
 * 
 */
package io.github.azanx.shopping_list.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import io.github.azanx.shopping_list.service.AppUserDetailsService;

/**
 * Configuration for web security
 * @author Kamil Piwowarski
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AppUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		AccessDeniedHandlerCustomImpl handler = new AccessDeniedHandlerCustomImpl();
		handler.setErrorPage("/accessDenied");
		return handler;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//
			.authorizeRequests()//
				.antMatchers("/register", "/resources/**", "/login", "/accessDenied").permitAll()//
				.anyRequest().authenticated()//
				.and()//
			.formLogin()//
				.loginPage("/login")//
				.usernameParameter("userName")//
				.passwordParameter("password")//
				.permitAll()//
				.and()//
			.exceptionHandling()//
				.accessDeniedHandler(accessDeniedHandler())//
				.and()//
			.httpBasic();//
	}
}
