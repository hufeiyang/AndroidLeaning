package com.hfy.test_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * APT test
 * https://juejin.cn/post/6922327435501535245#heading-0
 * https://juejin.cn/post/6947992544252788767
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface TestAnnotation {
    // Mark param's name or service name.
    String name() default "default name";
}