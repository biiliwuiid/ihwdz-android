package com.ihwdz.android.hwapp.ui.orders.logistics;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/08
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class LogisticModule {
    @Provides
    Activity provideActivity(LogisticActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(LogisticActivity activityContext){
        return activityContext;
    }

}
