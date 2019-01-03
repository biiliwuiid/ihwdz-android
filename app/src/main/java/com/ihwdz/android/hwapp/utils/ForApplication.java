package com.ihwdz.android.hwapp.utils;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/30
 * desc :
 * version: 1.0
 * </pre>
 */

@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}
