/**
 * 
 */
package io.github.azanx.shopping_list.service.exception;

import java.util.HashMap;

import io.github.azanx.shopping_list.controller.exception.MessageableException;

/**
 * Thrown if trying to add user with already used name
 * @author Kamil Piwowarski
 *
 */
public class DuplicateUserException extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6515080916120186375L;
	private final String userName;
	private final HashMap<String, String> parameters;
	
	public DuplicateUserException(String userName) {
		super("Cannot add user '" + userName + "' as user with this name already exists");
		this.userName = userName;

		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("message", this.getMessage());
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public HashMap<String, String> getParameters() {
		return this.parameters;
	}
}
