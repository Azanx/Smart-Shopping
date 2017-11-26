package io.github.azanx.shopping_list.service.exception;

public class ListNotFoundResponse {

	private int errorCode;
	private String userName;
	private long listId;
	private String message;

	
	public ListNotFoundResponse(int errorCode, String userName, long listId, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
		this.userName = userName;
		this.listId = listId;
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
	public long getListId() {
		return listId;
	}
}
