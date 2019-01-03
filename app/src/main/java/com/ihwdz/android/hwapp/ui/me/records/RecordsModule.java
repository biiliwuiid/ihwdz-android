package com.ihwdz.android.hwapp.ui.me.records;

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
public class RecordsModule {

    @Provides
    RecordContract.View provideView(RecordsActivity activityContext){return activityContext;}
    @Provides
    RecordContract.Presenter providePresenter(RecordPresenter presenter){
        return presenter;
    }

    @Provides
    Activity provideActivity(RecordsActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(RecordsActivity activityContext){
        return activityContext;
    }
}
