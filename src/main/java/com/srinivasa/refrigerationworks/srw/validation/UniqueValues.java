package com.srinivasa.refrigerationworks.srw.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * Holds multiple UniqueValue annotations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValues {

    /*
     * UniqueValue annotations array.
     */
    UniqueValue[] value();
}