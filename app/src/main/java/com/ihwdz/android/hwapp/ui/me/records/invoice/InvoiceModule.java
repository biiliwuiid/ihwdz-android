package com.ihwdz.android.hwapp.ui.me.records.invoice;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class InvoiceModule {

    @Provides
    InvoiceContract.View provideView(InvoiceActivity activityContext){return activityContext;}
    @Provides
    InvoiceContract.Presenter providePresenter(InvoicePresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(InvoiceActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(InvoiceActivity activityContext){
        return activityContext;
    }
}
