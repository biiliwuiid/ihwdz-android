package com.ihwdz.android.hwapp.ui.login;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/07
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class LoginPageModule {

    @Provides
    Activity provideActivity(LoginPageActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(LoginPageActivity activityContext){
        return activityContext;
    }
}
