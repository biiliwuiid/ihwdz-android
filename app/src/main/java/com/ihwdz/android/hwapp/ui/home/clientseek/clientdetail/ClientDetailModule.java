package com.ihwdz.android.hwapp.ui.home.clientseek.clientdetail;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/19
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class ClientDetailModule {

    @Provides
    ClientDetailContract.View provideView(ClientDetailActivity activityContext){return activityContext;}
    @Provides
    ClientDetailContract.Presenter providePresenter(ClientDetailPresenter presenter){
        return presenter;
    }
    @Provides
    Activity provideActivity(ClientDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ClientDetailActivity activityContext){
        return activityContext;
    }
}
