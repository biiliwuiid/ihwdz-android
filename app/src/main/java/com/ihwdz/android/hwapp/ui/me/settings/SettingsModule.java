package com.ihwdz.android.hwapp.ui.me.settings;

import android.app.Activity;
import android.content.Context;


import com.ihwdz.android.hwapp.ui.login.register.RegisterActivity;
import com.ihwdz.android.hwapp.ui.login.register.RegisterContract;
import com.ihwdz.android.hwapp.ui.login.register.RegisterPresenter;

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
public class SettingsModule {

    @Provides
    SettingContract.View provideView(SettingsActivity activityContext){return activityContext;}

    @Provides
    SettingContract.Presenter providePresenter(SettingPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(SettingsActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(SettingsActivity activityContext){
        return activityContext;
    }

}
