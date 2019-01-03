package com.ihwdz.android.hwapp.ui.me.messages;

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
public class MessagesModule {
    @Provides
    Activity provideActivity(MessagesActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(MessagesActivity activityContext){
        return activityContext;
    }

    @Provides
    MessagesContract.View provideView(MessagesActivity activityContext){return activityContext;}
    @Provides
    MessagesContract.Presenter providePresenter(MessagesPresenter presenter){
        return presenter;
    }

}
