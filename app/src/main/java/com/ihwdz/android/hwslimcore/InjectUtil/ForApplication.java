package com.ihwdz.android.hwslimcore.InjectUtil;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */

@Qualifier
@Retention(RUNTIME)
public @interface ForApplication {
}
