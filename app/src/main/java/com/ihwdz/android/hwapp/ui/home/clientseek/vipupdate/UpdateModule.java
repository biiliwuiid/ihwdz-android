package com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/18
 * desc :  升级会员
 * version: 1.0
 * </pre>
 */
@Module
public class UpdateModule {

    @Provides
    UpdateContract.View provideView(UpdateActivity activityContext){return activityContext;}
    @Provides
    UpdateContract.Presenter providePresenter(UpdatePresenter presenter){
        return presenter;
    }
    @Provides
    Activity provideActivity(UpdateActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(UpdateActivity activityContext){
        return activityContext;
    }
}
