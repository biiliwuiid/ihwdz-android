package com.ihwdz.android.hwapp.ui.orders.warehouse.buildwarehouse;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/16
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class WarehouseModule {

    @Provides
    Activity provideActivity(WarehouseActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(WarehouseActivity activityContext){
        return activityContext;
    }

    @Provides
    WarehouseContract.View provideView(WarehouseActivity activityContext){return activityContext;}

    @Provides
    WarehouseContract.Presenter providePresenter(WarehousePresenter presenter){
        return presenter;
    }

}
