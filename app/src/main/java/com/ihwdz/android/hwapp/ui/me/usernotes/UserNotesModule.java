package com.ihwdz.android.hwapp.ui.me.usernotes;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/18
 * desc :  用户须知
 * version: 1.0
 * </pre>
 */
@Module
public class UserNotesModule {
    @Provides
    Activity provideActivity(UserNotesActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(UserNotesActivity activityContext){
        return activityContext;
    }
}
