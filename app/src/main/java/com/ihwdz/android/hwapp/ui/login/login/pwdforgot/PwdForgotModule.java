package com.ihwdz.android.hwapp.ui.login.login.pwdforgot;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.ui.login.login.pwdforgot.PwdForgotActivity;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/17
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class PwdForgotModule {

    @Provides
    PwdForgotContract.View provideView(PwdForgotActivity activityContext){return activityContext;}
    @Provides
    PwdForgotContract.Presenter providePresenter(PwdForgotPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(PwdForgotActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PwdForgotActivity activityContext){
        return activityContext;
    }
}
