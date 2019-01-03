package com.ihwdz.android.hwapp.base.app;

import com.ihwdz.android.hwapp.base.mvp.BaseActivity;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */
//@Subcomponent(modules = {
//        AndroidInjectionModule.class,
//})
public interface BaseActivityComponent extends AndroidInjector<BaseActivity> {

    //每一个继承BaseActivity的Activity，都共享同一个SubComponent
//    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<BaseActivity> {

    }


}
