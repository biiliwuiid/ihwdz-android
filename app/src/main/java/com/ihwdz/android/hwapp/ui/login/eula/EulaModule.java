package com.ihwdz.android.hwapp.ui.login.eula;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;

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
public class EulaModule {
    @Provides
    Activity provideActivity(EulaActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(EulaActivity activityContext){
        return activityContext;
    }
}
