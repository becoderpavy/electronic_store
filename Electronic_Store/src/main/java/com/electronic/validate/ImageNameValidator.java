package com.electronic.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {

	private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		logger.info("Message from is valid :{}", value);
		if (value.isBlank()) {
			return false;
		} else {

			return true;
		}

	}

}
