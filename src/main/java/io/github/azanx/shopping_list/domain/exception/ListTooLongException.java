/**
 * 
 */
package io.github.azanx.shopping_list.domain.exception;

import java.util.HashMap;

import io.github.azanx.shopping_list.exception.MessageableException;

/**
 * @author Kamil Piwowarski
 *
 */
public class ListTooLongException  extends RuntimeException implements MessageableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4162895203034676713L;

	public static enum listType {
		SHOPPING_LIST,
		ITEM_LIST
	}
	
	private final int code;
	private final long listId; 
	private final listType type;
	private final HashMap<String, String> parameters;

	public ListTooLongException(listType type, long listId) {
		super("List too long! Requested list size is greater than maximum of '" + Short.MAX_VALUE +"' for '" + type + " with ID: '" + listId);
		this.code = 404;
		this.listId = listId;
		this.type = type;
		parameters = new HashMap<>();
		parameters.put("code", Integer.toString(code));
		parameters.put("listId", Long.toString(listId));
		parameters.put("listType", type.toString());
		parameters.put("message", this.getMessage());
		
	}
	
	public int getCode() {
		return code;
	}

	public long getListId() {
		return listId;
	}

	public listType getType() {
		return type;
	}

	@Override
	public HashMap<String, String> getParameters() {
		return parameters;
	}
}
