package com.ihwdz.android.hwapp.ui.home.clientseek.filter;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/24
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class ClientFilterModule {

    @Provides
    ClientFilterContract.View provideView(ClientFilterActivity activityContext){return activityContext;}
    @Provides
    ClientFilterContract.Presenter providePresenter(ClientFilterPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(ClientFilterActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ClientFilterActivity activityContext){
        return activityContext;
    }

}
