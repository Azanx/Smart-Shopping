/**
 * 
 */
package io.github.azanx.shopping_list.controller.exception;

import java.util.Map;

/**
 * Interface for exceptions which should be catched by ControllerAdvice and sent back to the client
 * @author Kamil Piwowarski
 *
 */

public interface MessageableException {

	/**
	 * Get Parameters of this exception
	 * @return Map of exception parameters and their values
	 */
	public Map<String,String> getParameters();
}
