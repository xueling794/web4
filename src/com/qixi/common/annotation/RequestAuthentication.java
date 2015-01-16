package com.qixi.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-1-14
 * Time: 下午10:35
 * To change this template use File | Settings | File Templates.
 */

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestAuthentication {
    // if need anthentication
    boolean isAuth() default true;
}
