package com.ihwdz.android.hwapp.ui.home.index;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class IndexModule {

    @Provides
    IndexContract.View provideView(IndexActivity activityContext){return activityContext;}
    @Provides
    IndexContract.Presenter providePresenter(IndexPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(IndexActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(IndexActivity activityContext){
        return activityContext;
    }
}
