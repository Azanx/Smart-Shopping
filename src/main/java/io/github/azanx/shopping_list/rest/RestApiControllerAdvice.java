package io.github.azanx.shopping_list.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.azanx.shopping_list.service.exception.DuplicateUserException;
import io.github.azanx.shopping_list.service.exception.DuplicateUserResponse;
import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import io.github.azanx.shopping_list.service.exception.ListNotFoundResponse;
import io.github.azanx.shopping_list.service.exception.UserNotFoundException;
import io.github.azanx.shopping_list.service.exception.UserNotFoundResponse;

@RestControllerAdvice("io.github.azanx.shopping_list.rest")
public class RestApiControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserNotFoundResponse> userNotFound(final UserNotFoundException e) {
		return new ResponseEntity<UserNotFoundResponse>(new UserNotFoundResponse(e.getCode(), e.getUserName(), e.getMessage()), HttpStatus.NOT_FOUND);
	}
		
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<DuplicateUserResponse> duplicateUser(final DuplicateUserException e) {
		return new ResponseEntity<DuplicateUserResponse>(new DuplicateUserResponse(e.getCode(), e.getUserName(), e.getMessage()), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ListNotFoundException.class)
	public ResponseEntity<ListNotFoundResponse> listNotFoud(final ListNotFoundException e) {
		return new ResponseEntity<ListNotFoundResponse>(new ListNotFoundResponse(e.getCode(), e.getUserName(), e.getListId(), e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
