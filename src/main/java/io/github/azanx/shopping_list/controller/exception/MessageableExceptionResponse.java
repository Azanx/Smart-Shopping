/**
 * 
 */
package io.github.azanx.shopping_list.controller.exception;

import java.util.HashMap;

/**
 * 
 * @author Kamil Piwowarski
 *
 */
public class MessageableExceptionResponse {
	
	private int error_code;
	private HashMap<String,String> parameters;

	public MessageableExceptionResponse(HashMap<String, String> parameters, int status) {
		super();
		this.parameters = parameters;
		this.error_code = status;
	}

	/**
	 * @return error code for this exception, mainly HttpStatus number
	 */
	public int getError_code() {
		return error_code;
	}
	
	/**
	 * Get Parameters of this exception
	 * @return Map of exception parameters and their values
	 */
	public HashMap<String, String> getParameters() {
		return parameters;
	}
}
