package io.github.azanx.shopping_list.domain.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Field has to be a valid, non-local, email address (something@something.something)
 * @author Kamil Piwowarski
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy=EmailVerificationValidator.class)
public @interface EmailVerification {

	String message() default "*E-mail is not valid";
	
	Class<?>[]groups() default {};
	Class<? extends Payload>[] payload() default {};
}
