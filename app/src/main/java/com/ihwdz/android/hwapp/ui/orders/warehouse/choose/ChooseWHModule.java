package com.ihwdz.android.hwapp.ui.orders.warehouse.choose;

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
public class ChooseWHModule {

    @Provides
    Activity provideActivity(ChooseWHActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ChooseWHActivity activityContext){
        return activityContext;
    }

    @Provides
    ChooseWHContract.View provideView(ChooseWHActivity activityContext){return activityContext;}

    @Provides
    ChooseWHContract.Presenter providePresenter(ChooseWHPresenter presenter){
        return presenter;
    }

}
