package com.trip.penguin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final String message = "UserNotFoundException";

	public UserNotFoundException() {
		super(message);
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}