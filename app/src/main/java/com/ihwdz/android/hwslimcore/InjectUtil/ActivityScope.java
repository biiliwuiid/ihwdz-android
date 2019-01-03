package com.ihwdz.android.hwslimcore.InjectUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :  Dagger2 局部单例 （work in THIS Activity ）
 * version: 1.0
 * </pre>
 */

@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
