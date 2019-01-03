package com.ihwdz.android.hwapp.ui.me.dealvip.apply;

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
public class QuotaModule {

    @Provides
    Activity provideActivity(QuotaActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(QuotaActivity activityContext){
        return activityContext;
    }
}
