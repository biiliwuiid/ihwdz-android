package com.ihwdz.android.hwapp.ui.guide;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/04
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class GuideModule {

    @Provides
    Activity provideActivity(GuideActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(GuideActivity activityContext){
        return activityContext;
    }
}
