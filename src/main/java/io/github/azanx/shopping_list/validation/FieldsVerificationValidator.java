package io.github.azanx.shopping_list.validation;

import java.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

/**
 * Validates whether two sibling fields have same value.
 * Fields can be char[] array
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
        boolean result = false;

        if(fieldValue != null)
        {
            //Passwords are kept as char arrays, other values mostly as Strings
            if(fieldValue instanceof char[])
                result =  Arrays.equals((char[])fieldValue, (char[])fieldMatchValue);
            else result = fieldValue.equals(fieldMatchValue);
        } else
            result = fieldMatchValue == null;
        return result;
    }
}
