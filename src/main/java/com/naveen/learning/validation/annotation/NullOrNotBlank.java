package com.naveen.learning.validation.annotation;

import com.naveen.learning.validation.validator.NullOrNotBlankValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})   //Target of the annotation(Filed/Method/Class)
@Retention(RetentionPolicy.RUNTIME) // annotation info is retained at runtime
@Documented  //indicates annotation is included in generated javadoc
@Constraint(validatedBy = NullOrNotBlankValidator.class)  //marks annotation as a validation constraint
public @interface NullOrNotBlank {

    //attributes within the Annotation
    String message() default "{javax.validation.constraints.Pattern.message}"; //default error message attribute

    Class<?>[] groups() default {}; //empty group for validation categorization

    Class<? extends Payload>[] payload() default {}; //typically used for adding metadata info
}
