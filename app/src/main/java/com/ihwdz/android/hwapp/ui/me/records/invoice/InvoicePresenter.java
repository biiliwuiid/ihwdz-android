package com.ihwdz.android.hwapp.ui.me.records.invoice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.InvoiceData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :  开票信息
 * version: 1.0
 * </pre>
 */
public class InvoicePresenter implements InvoiceContract.Presenter {

    String TAG = "InvoicePresenter";
    @Inject InvoiceActivity parentActivity;
    @Inject InvoiceContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    private boolean isSubmitClicked = false;

    private String orderId;
    private String amount;

    @Inject
    public InvoicePresenter(InvoiceActivity activity){
        this.parentActivity = activity;
        model = new LoginDataModel(parentActivity);
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
    public void setOrderId(String id) {
        this.orderId = id;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setAmount(String amount) {
        this.amount = amount;
    }


    @Override
    public void getInvoiceData() {

        Subscription rxSubscription = model
                .getInvoiceData(orderId)
                .compose(RxUtil.<InvoiceData>rxSchedulerHelper())
                .subscribe(new Subscriber<InvoiceData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError "+ e.toString());
                    }

                    @Override
                    public void onNext(InvoiceData data) {
                        Log.d(TAG,"onNext");
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                setAmount(data.data.amount);
                                mView.updateView(data.data);  // 更新开票信息
                            }
                        }else {
                            mView.showPromptMessage(data.msg);

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    @Override
    public void applyInvoiceData() {
        isSubmitClicked = true;
        Subscription rxSubscription = model
                .applyInvoiceData(orderId, amount)
                .compose(RxUtil.<InvoiceData>rxSchedulerHelper())
                .subscribe(new Subscriber<InvoiceData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG,"onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError "+ e.toString());
                        isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(InvoiceData data) {
                        Log.d(TAG,"onNext");
                        if (TextUtils.equals("0", data.code)){
                            mView.showPromptMessage(data.msg);
                            goBack();
                        }else {
                            mView.showPromptMessage(data.msg);
                            isSubmitClicked = false;
                            parentActivity.finish();

                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void goBack() {
        Constant.orderOptionDone = true; // 按钮操作完成
        parentActivity.onBackClicked();
    }
}
