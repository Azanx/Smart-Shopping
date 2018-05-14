package io.github.azanx.shopping_list.service.exception;

import java.util.HashMap;
import java.util.Map;

import io.github.azanx.shopping_list.controller.exception.MessageableException;

/**
 * Thrown if cannot find given user, it may have been removed
 * @author Kamil Piwowarski
 *
 */
public class UserNotFoundException extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;

	private final String userName;
	private final HashMap<String, String> parameters;

	public UserNotFoundException(String userName) {
		super("Couldn't find user: '" + userName + "'");
		this.userName = userName;
		
		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("message", this.getMessage());
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}
}
