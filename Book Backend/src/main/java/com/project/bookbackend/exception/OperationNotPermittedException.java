package com.project.bookbackend.exception;

public class OperationNotPermittedException extends RuntimeException {

	public OperationNotPermittedException(String onpExp) {
		super(onpExp);
	}
}
