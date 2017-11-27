/**
 * 
 */
package io.github.azanx.shopping_list.exception;

import java.util.HashMap;

/**
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

	public int getError_code() {
		return error_code;
	}
	
	public HashMap<String, String> getParameters() {
		return parameters;
	}
}
