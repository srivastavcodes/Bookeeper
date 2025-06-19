package com.project.bookbackend.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {

	NO_CODE(0, NOT_IMPLEMENTED, "No code."),

	INCORRECT_CURRENT_PASSWORD(300, BAD_REQUEST,
		"Incorrect password."),

	NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST,
		"Incorrect password."),

	ACCOUNT_LOCKED(302, FORBIDDEN,
		"User account is locked."),

	ACCOUNT_DISABLED(303, FORBIDDEN,
		"User account is disabled."),

	BAD_CREDENTIALS(304, FORBIDDEN,
		"Username and/or Password is incorrect.");

	private final int errorCode;
	private final HttpStatus httpStatus;
	private final String errDescription;

	BusinessErrorCodes(int errorCode, HttpStatus httpStatus, String errDescription) {
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
		this.errDescription = errDescription;
	}
}








