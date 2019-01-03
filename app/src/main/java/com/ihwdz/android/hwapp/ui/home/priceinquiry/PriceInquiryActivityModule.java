package com.ihwdz.android.hwapp.ui.home.priceinquiry;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/14
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class PriceInquiryActivityModule {


    @Provides
    PriceInquiryContract.View provideView(PriceInquiryActivity activityContext){return activityContext;}

    @Provides
    PriceInquiryContract.Presenter providePresenter(PricePresenter presenter){
        return presenter;
    }


    @Provides
    Activity provideActivity(PriceInquiryActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(PriceInquiryActivity activityContext){
        return activityContext;
    }


}
