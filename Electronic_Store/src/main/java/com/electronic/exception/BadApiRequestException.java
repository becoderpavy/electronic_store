package com.electronic.exception;

import lombok.Builder;

@Builder
public class BadApiRequestException extends RuntimeException {

	public BadApiRequestException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BadApiRequestException(String message) {
		super(message);

	}

}
