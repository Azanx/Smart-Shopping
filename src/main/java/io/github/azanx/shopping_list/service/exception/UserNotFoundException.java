package io.github.azanx.shopping_list.service.exception;

/**
 * @author Kamil Piwowarski
 *
 */
public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4321875051376072725L;
	private final int code;
	private final String userName;

	public UserNotFoundException(String userName) {
		super("Couldn't find user: '" + userName + "'");
		code = 404;
		this.userName = userName;
	}

	public int getCode() {
		return code;
	}

	public String getUserName() {
		return userName;
	}
}
