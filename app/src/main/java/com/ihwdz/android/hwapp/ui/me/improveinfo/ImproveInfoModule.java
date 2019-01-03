package com.ihwdz.android.hwapp.ui.me.improveinfo;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/15
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class ImproveInfoModule {

    @Provides
    Activity provideActivity(ImproveInfoActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ImproveInfoActivity activityContext){
        return activityContext;
    }

    @Provides
    ImproveInfoContract.View provideView(ImproveInfoActivity activityContext){return activityContext;}
    @Provides
    ImproveInfoContract.Presenter providePresenter(ImproveInfoPresenter presenter){
        return presenter;
    }


}
