package com.ihwdz.android.hwapp.ui.orders.detail;

import android.app.Activity;
import android.content.Context;


import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class OrderDetailModule {

    @Provides
    Activity provideActivity(OrderDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(OrderDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    OrderDetailContract.View provideView(OrderDetailActivity activityContext){return activityContext;}

    @Provides
    OrderDetailContract.Presenter providePresenter(OrderDetailPresenter presenter){
        return presenter;
    }

}
