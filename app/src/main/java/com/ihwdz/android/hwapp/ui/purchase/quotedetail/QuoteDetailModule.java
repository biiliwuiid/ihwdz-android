package com.ihwdz.android.hwapp.ui.purchase.quotedetail;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :
 * version: 1.0
 * </pre>
 */
@Module
public class QuoteDetailModule {
    @Provides
    Activity provideActivity(QuoteDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(QuoteDetailActivity activityContext){
        return activityContext;
    }

    @Provides
    QuoteDetailContract.View provideView(QuoteDetailActivity activityContext){return activityContext;}

    @Provides
    QuoteDetailContract.Presenter providePresenter(QuoteDetailPresenter presenter){
        return presenter;
    }

}
