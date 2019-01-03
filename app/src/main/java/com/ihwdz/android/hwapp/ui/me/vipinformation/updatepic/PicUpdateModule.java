package com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/17
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class PicUpdateModule {

    @Provides
    PicUpdateContract.View provideView(PicUpdateActivity activityContext){return activityContext;}

    @Provides
    PicUpdateContract.Presenter providePresenter(PicUpdatePresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(PicUpdateActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PicUpdateActivity activityContext){
        return activityContext;
    }
}
