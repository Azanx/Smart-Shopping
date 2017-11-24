package io.github.azanx.shopping_list.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("io.github.azanx.shopping_list.rest")
public class RestApiControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<UserNotFoundResponse> UserNotFound(final UserNotFoundException e) {
		//ClientErrorInformation error = new ClientErrorInformation(e.toString());
		return new ResponseEntity<UserNotFoundResponse>(new UserNotFoundResponse(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
	}
}
