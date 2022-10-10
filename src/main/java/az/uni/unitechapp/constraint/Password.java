package az.uni.unitechapp.constraint;

import az.uni.unitechapp.constant.HttpRequestValidationMessageKeyConstants;
import az.uni.unitechapp.constant.HttpRequestValidationRegexConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = HttpRequestValidationMessageKeyConstants.PASSWORD_CAN_NOT_BE_NULL)
@Pattern(regexp = HttpRequestValidationRegexConstants.PASSWORD, message = HttpRequestValidationMessageKeyConstants.PASSWORD_NOT_VALID)
public @interface Password {

    /**
     * Default error message if the request param is missing.
     *
     * @return : error message.
     */
    String message() default HttpRequestValidationMessageKeyConstants.PASSWORD_NOT_VALID;

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * The payload of the headers.
     *
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}
