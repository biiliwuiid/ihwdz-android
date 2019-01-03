package com.ihwdz.android.hwapp.ui.orders.filter;

import android.app.Activity;
import android.content.Context;

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
public class CityFilterModule {
    @Provides
    Activity provideActivity(CityFilterActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(CityFilterActivity activityContext){
        return activityContext;
    }

    @Provides
    CityFilterContract.View provideView(CityFilterActivity activityContext){
        return activityContext;
    }

    @Provides
    CityFilterContract.Presenter providePresenter(CityFilterPresenter presenter){
        return presenter;
    }
}
