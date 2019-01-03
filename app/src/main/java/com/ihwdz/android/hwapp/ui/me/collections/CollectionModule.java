package com.ihwdz.android.hwapp.ui.me.collections;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/12
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class CollectionModule {

    @Provides
    CollectionContract.View provideView(CollectionsActivity activityContext){return activityContext;}
    @Provides
    CollectionContract.Presenter providePresenter(CollectionPresenter presenter){
        return presenter;
    }
    @Provides
    Activity provideActivity(CollectionsActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(CollectionsActivity activityContext){
        return activityContext;
    }
}
