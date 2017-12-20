/**
 * 
 */
package io.github.azanx.shopping_list.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kamil Piwowarski
 *
 */
@Configuration
@ComponentScan(basePackages = {"io.github.azanx.shopping_list.service"})
public class ServiceConfig {

}
