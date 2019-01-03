package com.ihwdz.android.hwapp.ui.home.priceinquiry.collections;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/21
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class PriceCollectionModule {

    @Provides
    PriceCollectionContract.View provideView(PriceCollectionActivity activityContext){return activityContext;}

    @Provides
    PriceCollectionContract.Presenter providePresenter(PriceCollectionPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(PriceCollectionActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PriceCollectionActivity activityContext){
        return activityContext;
    }
}
