package com.ihwdz.android.hwapp.ui.main.advertisement;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/29
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class AdvertisementModule {

    @Provides
    Activity provideActivity(AdvertisementActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(AdvertisementActivity activityContext){
        return activityContext;
    }
}
