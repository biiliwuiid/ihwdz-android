package com.ihwdz.android.hwapp.ui.login.register;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.model.bean.UserData;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class RegisterModule {

    @Provides
    RegisterContract.View provideView(RegisterActivity activityContext){return activityContext;}
    @Provides
    RegisterContract.Presenter providePresenter(RegisterPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(RegisterActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(RegisterActivity activityContext){
        return activityContext;
    }

    @Provides
    UserData.UserEntity provideUserEntity(RegisterActivity activityContext){return new UserData.UserEntity(); }
}
