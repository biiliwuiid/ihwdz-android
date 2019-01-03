package com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/17
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class PurchaseListModule {

    @Provides
    PurchaseListContract.View provideView(PurchaseListActivity activityContext){return activityContext;}
    @Provides
    PurchaseListContract.Presenter providePresenter(PurchaseListPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(PurchaseListActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PurchaseListActivity activityContext){
        return activityContext;
    }
}
