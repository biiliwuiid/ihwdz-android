package com.ihwdz.android.hwapp.ui.me.dealvip.deposit;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/17
 * desc :   保证金
 * version: 1.0
 * </pre>
 */
@Module
public class DepositModule {

    @Provides
    DepositContract.View provideView(DepositActivity activityContext){return activityContext;}
    @Provides
    DepositContract.Presenter providePresenter(DepositPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(DepositActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(DepositActivity activityContext){
        return activityContext;
    }

}
