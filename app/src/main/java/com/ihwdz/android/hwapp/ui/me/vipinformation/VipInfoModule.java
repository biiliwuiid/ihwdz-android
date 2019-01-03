package com.ihwdz.android.hwapp.ui.me.vipinformation;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/12
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class VipInfoModule {
    @Provides
    VipInfoContract.View provideView(VipInfoActivity activityContext){return activityContext;}

    @Provides
    VipInfoContract.Presenter providePresenter(VipInfoPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(VipInfoActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(VipInfoActivity activityContext){
        return activityContext;
    }

}
