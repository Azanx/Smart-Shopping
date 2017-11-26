/**
 * 
 */
package io.github.azanx.shopping_list.service.exception;

/**
 * @author Kamil Piwowarski
 *
 */
public class DuplicateUserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6515080916120186375L;
	private final int code;
	private final String userName;
	
	public DuplicateUserException(String userName) {
		super("Cannot add user '" + userName + "' as user with this name already exists");
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
