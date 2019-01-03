package com.ihwdz.android.hwapp.ui.orders.extension;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :  申请展期
 * version: 1.0
 * </pre>
 */
@Module
public class ExtensionModule {

    @Provides
    Activity provideActivity(ExtensionActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ExtensionActivity activityContext){
        return activityContext;
    }

    @Provides
    ExtensionContract.View provideView(ExtensionActivity activityContext){return activityContext;}

    @Provides
    ExtensionContract.Presenter providePresenter(ExtensionPresenter presenter){return presenter;}

}
