package com.ihwdz.android.hwapp.ui.purchase.reviewquote;

import android.app.Activity;
import android.content.Context;

import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailActivity;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailContract;
import com.ihwdz.android.hwapp.ui.purchase.quotedetail.QuoteDetailPresenter;

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
public class ReviewModule {
    @Provides
    Activity provideActivity(ReviewActivity activityContext){
        return activityContext;
    }

    @Provides
    Context provideContext(ReviewActivity activityContext){
        return activityContext;
    }

    @Provides
    ReviewContract.View provideView(ReviewActivity activityContext){return activityContext;}

    @Provides
    ReviewContract.Presenter providePresenter(ReviewPresenter presenter){
        return presenter;
    }

}
