package com.renameyourappname.mobile.injector.annotation;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Kobe on 2017/12/25.
 */
@Scope
@Retention(RUNTIME)
public @interface PerActivity {
}
