package com.ihwdz.android.hwapp.ui.home.moodindex;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/04
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class MoodIndexModule {
    @Provides
    Activity provideActivity(MoodIndexActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(MoodIndexActivity activityContext){
        return activityContext;
    }
}
