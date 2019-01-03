package com.ihwdz.android.hwapp.ui.me.businessvip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.collections.CollectionsActivity;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoActivity;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesActivity;
import com.ihwdz.android.hwapp.ui.me.settings.SettingsActivity;
import com.ihwdz.android.hwapp.ui.me.vipinformation.VipInfoActivity;
import com.ihwdz.android.hwapp.ui.orders.logistics.LogisticActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/17
 * desc :
 * version: 1.0
 * </pre>
 */
public class BusinessVipPresenter implements BusinessVipContract.Presenter {

    String TAG = "DealVipPresenter";

    private MainActivity activity;
    private BusinessVipContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;


    public BusinessVipPresenter(BusinessVipContract.View view){
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        if (mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
        if (activity != null) {
            activity = null;
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
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void gotoCollections() {
        CollectionsActivity.startCollectionsActivity(activity.getBaseContext());
    }

    @Override
    public void gotoMsgCenter() {
        Intent intent = new Intent(activity.getBaseContext(), MessagesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoUpdateUserInfo() {
        Intent intent = new Intent(activity.getBaseContext(), ImproveInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoMyVipInfo() {
        Intent intent = new Intent(activity.getBaseContext(),VipInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoLogistics() {
        Intent intent = new Intent(activity, LogisticActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoSettings() {
        SettingsActivity.startSettingsActivity(activity.getBaseContext(),2);
    }

}
