package com.ihwdz.android.hwapp.ui.me.infoupdate;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoActivity;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoContract;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoPresenter;

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
public class InfoUpdateModule {
    @Provides
    Activity provideActivity(InfoUpdateActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(InfoUpdateActivity activityContext){
        return activityContext;
    }

    @Provides
    InfoUpdateContract.View provideView(InfoUpdateActivity activityContext){return activityContext;}
    @Provides
    InfoUpdateContract.Presenter providePresenter(InfoUpdatePresenter presenter){
        return presenter;
    }

}
