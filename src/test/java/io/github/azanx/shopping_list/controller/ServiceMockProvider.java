/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import io.github.azanx.shopping_list.service.RepositoryService;

/**
 * Provides mock for RepositoryService Bean
 * @author Kamil Piwowarski
 *
 */
@Profile("MvcController-test")
@Configuration
public class ServiceMockProvider {

	@Bean
	@Primary
	public RepositoryService repositoryService() {
		return Mockito.mock(RepositoryService.class);
	}
}
