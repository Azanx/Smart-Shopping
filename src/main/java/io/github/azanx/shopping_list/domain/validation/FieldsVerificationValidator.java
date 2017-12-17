package io.github.azanx.shopping_list.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Validates whether two sibling fields have same value
 * @author Kamil Piwowarski
 *
 */
public class FieldsVerificationValidator implements ConstraintValidator<FieldsVerification, Object> {

	private String field;
	private String fieldMatch;

	@Override
	public void initialize(FieldsVerification constraintAnnotation) {
		field = constraintAnnotation.field();
		fieldMatch = constraintAnnotation.fieldMatch();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
		Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
		
		if(fieldValue != null)
			return fieldValue.equals(fieldMatchValue);
		
		return fieldMatchValue == null;
	}
}
