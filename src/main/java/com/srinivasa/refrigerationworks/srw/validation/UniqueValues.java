package com.srinivasa.refrigerationworks.srw.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Container annotation for holding multiple UniqueValue annotations.
 * - Allows multiple UniqueValue annotations to be applied to the same element (e.g., class).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValues {

    /*
     * Array of UniqueValue annotations.
     */
    UniqueValue[] value();
}