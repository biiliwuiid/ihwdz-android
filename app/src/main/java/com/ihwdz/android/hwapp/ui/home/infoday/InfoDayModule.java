package com.ihwdz.android.hwapp.ui.home.infoday;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class InfoDayModule {

    @Provides
    InfoDayContract.View provideView(InfoDayActivity activityContext){return activityContext;}
    @Provides
    InfoDayContract.Presenter providePresenter(InfoDayPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(InfoDayActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(InfoDayActivity activityContext){
        return activityContext;
    }
}
