package com.ihwdz.android.hwapp.ui.me.improveinfo;

import android.os.Bundle;

import com.ihwdz.android.hwapp.ui.me.messages.MessagesActivity;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesContract;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/30
 * desc :  完善用户信息
 * version: 1.0
 * </pre>
 */
public class ImproveInfoPresenter implements ImproveInfoContract.Presenter{

    String TAG = "ImproveInfoPresenter";

    @Inject ImproveInfoActivity parentActivity;
    @Inject ImproveInfoContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    @Inject
    public ImproveInfoPresenter(ImproveInfoActivity activity){
        this.parentActivity = activity;
        model = LoginDataModel.getInstance(parentActivity);
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
    public void gotoUpdateName() {

    }

    @Override
    public void gotoUpdateEmail() {

    }

    @Override
    public void updateAddress() {

    }

    @Override
    public void gotoUpdateDetailAddress() {

    }
}
