package com.ihwdz.android.hwapp.ui.orders.payment;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/29
 * desc :  支付页面 （选择支付宝/微信 支付）
 * version: 1.0
 * </pre>
 */
@Module
public class PaymentModule {

    @Provides
    Activity provideActivity(PaymentActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PaymentActivity activityContext){
        return activityContext;
    }

    @Provides
    PaymentContract.View provideView(PaymentActivity activityContext){return activityContext;}

    @Provides
    PaymentContract.Presenter providePresenter(PaymentPresenter presenter){return presenter;}

}
