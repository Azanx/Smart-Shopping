package io.github.azanx.shopping_list.rest;

public class UserNotFoundResponse {

	private int errorCode;
	private String message;
	
	public UserNotFoundResponse(int errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public String getMessage() {
		return message;
	}
}
