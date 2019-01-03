package com.ihwdz.android.hwapp.ui.orders.confirm;

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
public class OrderConfirmModule {

    @Provides
    Activity provideActivity(OrderConfirmActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(OrderConfirmActivity activityContext){
        return activityContext;
    }

    @Provides
    OrderConfirmContract.View provideView(OrderConfirmActivity activityContext){return activityContext;}

    @Provides
    OrderConfirmContract.Presenter providePresenter(OrderConfirmPresenter presenter){
        return presenter;
    }
}
