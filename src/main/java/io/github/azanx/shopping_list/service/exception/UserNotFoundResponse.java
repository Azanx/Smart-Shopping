package io.github.azanx.shopping_list.service.exception;

public class UserNotFoundResponse {

	private int errorCode;
	private String userName;
	private String message;
	
	public UserNotFoundResponse(int errorCode, String userName, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
		this.userName = userName;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
	public String getUserName() {
		return userName;
	}
}
