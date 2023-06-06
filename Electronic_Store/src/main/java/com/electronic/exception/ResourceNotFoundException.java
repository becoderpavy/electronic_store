package com.electronic.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		super();

	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}
