package com.ihwdz.android.hwapp.ui.main;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/15
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class MainActivityModule {

    @Provides
    static String provideName() {
        return MainActivity.class.getName();
    }
    @Provides
    MainContract.View provideView(MainActivity activityContext){return activityContext;}

    @Provides
    MainContract.Presenter providePresenter(MainPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(MainActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(MainActivity activityContext){
        return activityContext;
    }

}
