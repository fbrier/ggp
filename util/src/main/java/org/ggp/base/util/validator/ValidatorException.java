package org.ggp.base.util.validator;

@SuppressWarnings("serial")
public class ValidatorException extends Exception {
	public ValidatorException(String explanation) {
		super("Validator: " + explanation);
	}
}
