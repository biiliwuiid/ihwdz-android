package com.ihwdz.android.hwapp.ui.orders.query;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LogisticsResultData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/09
 * desc :
 * version: 1.0
 * </pre>
 */
public class QueryPresenter implements QueryContract.Presenter {

    String TAG = "QueryPresenter";

    @Inject QueryResultActivity parentActivity;
    @Inject QueryContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    String currentAmount = "";
    @Inject
    public QueryPresenter(QueryResultActivity activity){
        this.parentActivity = activity;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if(mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
        if(parentActivity != null){
            parentActivity = null;
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void store(Bundle outState) {

    }

    @Override
    public void restore(Bundle inState) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void getPriceData() {
        Log.e(TAG, "amount: " + getCurrentAmount());
        int amount = Integer.parseInt(getCurrentAmount());
        LogisticsModel model = new LogisticsModel(parentActivity);
        Subscription rxSubscription = model
                .getPriceData(Constant.provFrom, Constant.cityFrom, Constant.distinctFrom,
                        Constant.provTo, Constant.cityTo, Constant.distinctTo,
                        amount
                        )
                .compose(RxUtil.<LogisticsResultData>rxSchedulerHelper())
                .subscribe(new Subscriber<LogisticsResultData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(LogisticsResultData data) {
                        Log.d(TAG, "onNext");
                        if ( data.code == 0 && data.data != null){
                            mView.updateView(data.data);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setCurrentAmount(String amount) {

        this.currentAmount = amount;
    }

    @Override
    public String getCurrentAmount() {
        return currentAmount;
    }
}
