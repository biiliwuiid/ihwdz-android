package com.ihwdz.android.hwapp.ui.login.login;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.model.bean.UserData;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/05
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class LoginModule {

    @Provides
    LoginContract.View provideView(LoginActivity activityContext){return activityContext;}
    @Provides
    LoginContract.Presenter providePresenter(LoginPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(LoginActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(LoginActivity activityContext){
        return activityContext;
    }

    @Provides
    UserData.UserEntity provideUserEntity(LoginActivity activityContext){return new UserData.UserEntity(); }

}
