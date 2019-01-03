package com.ihwdz.android.hwapp.ui.home.detail;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/01
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class NewsDetailModule {

    @Provides
    NewsDetailContract.View provideView(NewsDetailActivity activityContext){return activityContext;}
    @Provides
    NewsDetailContract.Presenter providePresenter(NewsDetailPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(NewsDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(NewsDetailActivity activityContext){
        return activityContext;
    }
}
