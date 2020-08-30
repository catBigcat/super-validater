package apaas.support.annotion;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import apaas.support.validator.MapNotNullValidator;
import apaas.support.validator.MapStringNotNUllValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy= MapStringNotNUllValidator.class)
public @interface MapStringNotNull {
	String[] value();
	String message() default "{paas.support.validator.MapNotNull.message}";
	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
