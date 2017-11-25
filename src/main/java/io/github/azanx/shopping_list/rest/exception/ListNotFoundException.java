package io.github.azanx.shopping_list.rest.exception;

//@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="Could not find requested user")
public class ListNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final int code;
	private final String userName;

	public ListNotFoundException(Long id, String userName) {
		super("Couldn't find list '" + id + "' for user: '" + userName + "'");
		this.code = 404;
		this.userName = userName;
	}
	
	public ListNotFoundException(String userName) {
		super("Couldn't find any list for user: '" + userName + "'");
		this.code = 404;
		this.userName = userName;
	}

	public int getCode() {
		return code;
	}
}
