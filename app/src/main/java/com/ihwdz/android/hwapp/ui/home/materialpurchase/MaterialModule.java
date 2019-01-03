package com.ihwdz.android.hwapp.ui.home.materialpurchase;

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
public class MaterialModule {

    @Provides
    MaterialContract.View provideView(MaterialActivity activityContext){return activityContext;}
    @Provides
    MaterialContract.Presenter providePresenter(MaterialPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(MaterialActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(MaterialActivity activityContext){
        return activityContext;
    }
}
