package io.github.azanx.shopping_list.service.exception;

import java.util.HashMap;
import java.util.Map;

import io.github.azanx.shopping_list.controller.exception.MessageableException;

/**
 * Thrown if cannot find given item, it may have been removed
 * @author Kamil Piwowarski
 *
 */
public class ItemNotFoundException extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final String userName;
	private final short itemNo; //-1 if there are no items
	private final HashMap<String, String> parameters;

	public ItemNotFoundException(short itemNo, String userName) {
		super("Couldn't find list '" + itemNo + "' for user: '" + userName + "'");
		this.userName = userName;
		this.itemNo = itemNo;

		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Short.toString(itemNo));
		parameters.put("message", this.getMessage());
	}
	
	public ItemNotFoundException(String userName) {
		super("Couldn't find any list for user: '" + userName + "'");
		this.userName = userName;
		this.itemNo = -1;
		parameters = new HashMap<>();
		parameters.put("userName", userName);
		parameters.put("listId", Short.toString(itemNo));
		parameters.put("message", this.getMessage());
	}

	public String getUserName() {
		return userName;
	}

	public long getItemNo() {
		return itemNo;
	}

	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}
}
