package apaas.support.annotion;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

import apaas.support.validator.MapNotNullValidator;

/**
 * 
 * @author lxsmac </p>
 * 输入一定是一个string 或者map，</p>
 * 或者是一个map的数组。</p>
 * 验证map中某个字端不为空。</p>
 * 例如  example.test</p>
 * 等价于 验证 </p>
 * next =  map.get("example")  </p>
 * assert next != null</p>
 * test = map.get("test")</p>
 * assert test != null</p>
 *
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy= MapNotNullValidator.class)
public @interface MapNotNull {
	String[] value();
	String message() default "{paas.support.validator.MapNotNull.message}";

	Class<?>[] groups() default { };
	Class<? extends Payload>[] payload() default { };
}
