package com.ihwdz.android.hwapp.ui.purchase.purchasedetail;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class PurchaseDetailModule {

    @Provides
    Activity provideActivity(PurchaseDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PurchaseDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    PurchaseDetailContract.View provideView(PurchaseDetailActivity activityContext){return activityContext;}

    @Provides
    PurchaseDetailContract.Presenter providePresenter(PurchaseDetailPresenter presenter){
        return presenter;
    }

}
