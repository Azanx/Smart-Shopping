package io.github.azanx.shopping_list.service.exception;

import java.util.HashMap;
import java.util.Map;

import io.github.azanx.shopping_list.controller.exception.MessageableException;

/**
 * Thrown if cannot find given list, it may have been removed
 * @author Kamil Piwowarski
 *
 */
public class ListNotFoundException extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final String userName;
	private final long listId; //-1 if there are no lists
	private final HashMap<String, String> parameters;

	public ListNotFoundException(long listId, String userName) {
		super("Couldn't find list '" + listId + "' for user: '" + userName + "'");
		this.userName = userName;
		this.listId = listId;

		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Long.toString(listId));
		parameters.put("message", this.getMessage());
	}
	
	public ListNotFoundException(String userName) {
		super("Couldn't find any list for user: '" + userName + "'");
		this.userName = userName;
		this.listId = -1;
		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Long.toString(listId));
		parameters.put("message", this.getMessage());
	}

	public String getUserName() {
		return userName;
	}

	public long getListId() {
		return listId;
	}

	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}
}
