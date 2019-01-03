package com.ihwdz.android.hwapp.ui.home.index;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/24
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class IndexWebActivityModule {

    @Provides
    Activity provideActivity(IndexWebActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(IndexWebActivity activityContext){
        return activityContext;
    }
}
