package com.ihwdz.android.hwapp.ui.me.businessvip.myquote;

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
public class QuoteModule {
    @Provides
    Activity provideActivity(QuoteActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(QuoteActivity activityContext){
        return activityContext;
    }
}
