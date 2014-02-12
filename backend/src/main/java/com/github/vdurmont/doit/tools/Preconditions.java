package com.github.vdurmont.doit.tools;

import com.github.vdurmont.doit.exception.IllegalInputException;
import org.hibernate.validator.internal.constraintvalidators.EmailValidator;

public class Preconditions {
	public static void checkNotNull(Object obj, String parameterName) {
		if (obj == null)
			throw new IllegalInputException("Null parameter: " + parameterName);
	}

	public static void checkEmail(String email, String parameterName) {
		checkNotEmpty(email, parameterName);
		if (!new EmailValidator().isValid(email, null))
			throw new IllegalInputException("Invalid email: " + parameterName);
	}

	public static void checkNotEmpty(String str, String parameterName) {
		checkNotNull(str, parameterName);
		if (str.isEmpty())
			throw new IllegalInputException("Empty string: " + parameterName);
	}
}
