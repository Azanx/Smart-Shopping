package io.github.azanx.shopping_list.service.exception;

import java.util.HashMap;

import io.github.azanx.shopping_list.exception.MessageableException;

//@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Could not find requested user")
public class ItemNotFoundException extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final String userName;
	private final long itemId; //-1 if there are no items
	private final HashMap<String, String> parameters;

	public ItemNotFoundException(long itemId, String userName) {
		super("Couldn't find list '" + itemId + "' for user: '" + userName + "'");
		this.userName = userName;
		this.itemId = itemId;

		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Long.toString(itemId));
		parameters.put("message", this.getMessage());
	}
	
	public ItemNotFoundException(String userName) {
		super("Couldn't find any list for user: '" + userName + "'");
		this.userName = userName;
		this.itemId = -1;
		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Long.toString(itemId));
		parameters.put("message", this.getMessage());
	}

	public String getUserName() {
		return userName;
	}

	public long getItemId() {
		return itemId;
	}

	@Override
	public HashMap<String, String> getParameters() {
		return this.parameters;
	}
}
