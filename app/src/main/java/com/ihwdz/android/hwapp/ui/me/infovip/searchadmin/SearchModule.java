package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class SearchModule {
    @Provides
    SearchContract.View provideView(SearchActivity activityContext){return activityContext;}
    @Provides
    SearchContract.Presenter providePresenter(SearchPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(SearchActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(SearchActivity activityContext){
        return activityContext;
    }
}
