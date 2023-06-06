package com.electronic.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.electronic.dto.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private List<ObjectError> allErrors;

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(WebRequest request, ResourceNotFoundException ex) {

		logger.info("Exception handler invoked");
		ApiResponseMessage error = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.NOT_FOUND)
				.success(true).build();
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<?> BadApiRequestException(WebRequest request, BadApiRequestException ex) {

		logger.info("Bad Api Request");
		ApiResponseMessage error = ApiResponseMessage.builder().message(ex.getMessage()).status(HttpStatus.BAD_REQUEST)
				.success(false).build();
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		allErrors = ex.getBindingResult().getAllErrors();
		Map<String, Object> res = new HashMap<String, Object>();

		allErrors.stream().forEach(e -> {
			String message = e.getDefaultMessage();
			String field = ((FieldError) (e)).getField();

			res.put(field, message);
		});

		return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);

	}

}
