package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/13
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class ApplyProgressModule {

    @Provides
    ApplyProgressContract.View provideView(ApplyProgressActivity activityContext){return activityContext;}
    @Provides
    ApplyProgressContract.Presenter providePresenter(ApplyProgressPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(ApplyProgressActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ApplyProgressActivity activityContext){
        return activityContext;
    }
}
