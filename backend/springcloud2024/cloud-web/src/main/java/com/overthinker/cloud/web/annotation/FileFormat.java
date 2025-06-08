package com.overthinker.cloud.web.annotation;

import com.overthinker.cloud.web.validators.FileFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = FileFormatValidator.class)
@Target({PARAMETER, FIELD})
@Retention(RUNTIME)
public @interface FileFormat {
    String message() default "文件格式不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowedExtensions();
}