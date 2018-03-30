package com.chad.library.adapter.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * https://github.com/chaychan
 * @author ChayChan
 * @description: ItemProvider's annotation
 * @date 2018/3/21  10:48
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemProviderTag {
    int viewType();
    int layout();
}
