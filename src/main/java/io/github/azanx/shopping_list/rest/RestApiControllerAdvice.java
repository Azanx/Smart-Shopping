package io.github.azanx.shopping_list.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.azanx.shopping_list.rest.exception.ListNotFoundException;
import io.github.azanx.shopping_list.rest.exception.ListNotFoundResponse;
import io.github.azanx.shopping_list.rest.exception.UserNotFoundException;
import io.github.azanx.shopping_list.rest.exception.UserNotFoundResponse;

@RestControllerAdvice("io.github.azanx.shopping_list.rest")
public class RestApiControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserNotFoundResponse> userNotFound(final UserNotFoundException e) {
		//ClientErrorInformation error = new ClientErrorInformation(e.toString());
		return new ResponseEntity<UserNotFoundResponse>(new UserNotFoundResponse(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ListNotFoundException.class)
	public ResponseEntity<ListNotFoundResponse> listNotFoud(final ListNotFoundException e) {
		return new ResponseEntity<ListNotFoundResponse>(new ListNotFoundResponse(e.getCode(), e.getUserName(), e.getListId(), e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
