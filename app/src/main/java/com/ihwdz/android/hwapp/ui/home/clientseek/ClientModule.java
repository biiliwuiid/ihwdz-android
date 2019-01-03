package com.ihwdz.android.hwapp.ui.home.clientseek;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/20
 * desc :
 * version: 1.0
 * </pre>
 */

@Module
public class ClientModule {

    @Provides
    ClientContract.View provideView(ClientActivity activityContext){return activityContext;}
    @Provides
    ClientContract.Presenter providePresenter(ClientPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(ClientActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ClientActivity activityContext){
        return activityContext;
    }

}
