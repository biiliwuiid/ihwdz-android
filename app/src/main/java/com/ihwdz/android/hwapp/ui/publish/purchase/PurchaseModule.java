package com.ihwdz.android.hwapp.ui.publish.purchase;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailActivity;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailPresenter;

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
public class PurchaseModule {

    @Provides
    Activity provideActivity(PurchaseActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PurchaseActivity activityContext){
        return activityContext;
    }

    @Provides
    PurchaseContract.View provideView(PurchaseActivity activityContext){return activityContext;}

    @Provides
    PurchaseContract.Presenter providePresenter(PurchasePresenter presenter){
        return presenter;
    }


}
