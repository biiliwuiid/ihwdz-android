package com.ihwdz.android.hwapp.ui.me.feedback;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/12
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class FeedbackModule {

    @Provides
    Activity provideActivity(FeedbackActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(FeedbackActivity activityContext){
        return activityContext;
    }
}
