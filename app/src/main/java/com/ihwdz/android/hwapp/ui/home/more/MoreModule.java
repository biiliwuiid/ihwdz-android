package com.ihwdz.android.hwapp.ui.home.more;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/30
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class MoreModule {

    @Provides
    MoreContract.View provideView(MoreActivity activityContext){return activityContext;}
    @Provides
    MoreContract.Presenter providePresenter(MorePresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(MoreActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(MoreActivity activityContext){
        return activityContext;
    }
}
