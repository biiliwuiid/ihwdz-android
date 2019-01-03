package com.ihwdz.android.hwapp.ui.orders.query;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/08
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class QueryResultModule {

    @Provides
    Activity provideActivity(QueryResultActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(QueryResultActivity activityContext){
        return activityContext;
    }

    @Provides
    QueryContract.View provideView(QueryResultActivity activityContext){
        return activityContext;
    }

    @Provides
    QueryContract.Presenter providePresenter(QueryPresenter presenter){
        return presenter;
    }
}
