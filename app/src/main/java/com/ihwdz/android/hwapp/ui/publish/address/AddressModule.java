package com.ihwdz.android.hwapp.ui.publish.address;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/23
 * desc :   收货地址
 * version: 1.0
 * </pre>
 */
@Module
public class AddressModule {

    @Provides
    Activity provideActivity(AddressActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(AddressActivity activityContext){
        return activityContext;
    }

    @Provides
    AddressContract.View provideView(AddressActivity activityContext){return activityContext;}

    @Provides
    AddressContract.Presenter providePresenter(AddressPresenter presenter){
        return presenter;
    }


}
