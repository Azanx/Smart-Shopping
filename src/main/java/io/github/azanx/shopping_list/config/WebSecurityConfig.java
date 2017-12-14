/**
 * 
 */
package io.github.azanx.shopping_list.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuration for web security
 * @author Kamil Piwowarski
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("test").password("test").roles("USER").build());
		return manager;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//
			.authorizeRequests()//
				.antMatchers("/register", "/resources/**", "/login").permitAll()//
				.anyRequest().authenticated()//
				.and()//
			.formLogin()//
				.loginPage("/login")//
				.usernameParameter("userName")//
				.passwordParameter("password")//
				.permitAll()//
				.and()//
			.httpBasic();//
	}
}
