package io.github.azanx.shopping_list.rest.exception;

//@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Could not find requested user")
public class ListNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final int code;
	private final String userName;
	private final long listId; //-1 if there are no lists

	public ListNotFoundException(long listId, String userName) {
		super("Couldn't find list '" + listId + "' for user: '" + userName + "'");
		this.code = 404;
		this.userName = userName;
		this.listId = listId;
	}
	
	public ListNotFoundException(String userName) {
		super("Couldn't find any list for user: '" + userName + "'");
		this.code = 404;
		this.userName = userName;
		this.listId = -1;
	}

	public int getCode() {
		return code;
	}

	public String getUserName() {
		return userName;
	}

	public long getListId() {
		return listId;
	}
}
