package io.github.azanx.shopping_list.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.EmailValidator;



/**
 * @author Kamil Piwowarski
 * Validates e-mail address to be valid non-local email
 */
public class EmailVerificationValidator implements ConstraintValidator<EmailVerification, String> {

	
	private String message;

	@Override
	public void initialize(EmailVerification constraintAnnotation) {
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		if((email == null) || (!emailValidator.isValid(email)))
			return false;
		return true;
	}

	
}
