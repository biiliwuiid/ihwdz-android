package com.ihwdz.android.hwapp.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.HwVIPCN.HwVipCNActivity;
import com.ihwdz.android.hwapp.ui.me.aboutus.AboutActivity;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesActivity;
import com.ihwdz.android.hwapp.ui.orders.logistics.LogisticActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :
 * version: 1.0
 * </pre>
 */
public class UserPresenter implements UserContract.Presenter {

    String TAG = "UserPresenter";

    private MainActivity activity;
    private UserContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    Boolean isLoginPageShown = false;

    public UserPresenter(UserContract.View view) {
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
        //outState.putBoolean("isLoginPageShown", isLoginPageShown);
    }

    @Override
    public void restore(Bundle inState) {
        //setIsLoginPageShown(inState.getBoolean("isLoginPageShown"));
    }

    @Override
    public void bindActivity(MainActivity activity) {
        this.activity = activity;
    }

//    @Override
//    public boolean getIsLoginPageShown() {
//        return isLoginPageShown;
//    }
//
//    // 判断是否需要显示登录页面
//    @Override
//    public void setIsLoginPageShown(Boolean isLoginPageShown) {
//        this.isLoginPageShown = isLoginPageShown;
//        if (isLoginPageShown) {
//            // 未登录状态 提示登录
//            gotoLoginPage();
//        }
//    }

    @Override
    public void gotoLoginPage() {
        LoginPageActivity.startLoginPageActivity(activity, true);
    }

    @Override
    public void gotoCollections() {
        // 未登录状态 提示登录
        gotoLoginPage();
    }

    // 消息中心不需要登录
    @Override
    public void gotoMsgCenter() {
//        // 未登录状态 提示登录
//        gotoLoginPage();
        Intent intent = new Intent(activity.getBaseContext(), MessagesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoLogistics() {
        Intent intent = new Intent(activity, LogisticActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoHwVipCn() {
        Intent intent = new Intent(activity.getBaseContext(), HwVipCNActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoAboutUs() {
        Intent intent = new Intent(activity.getBaseContext(), AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoSettings() {
        // 未登录状态 提示登录
        gotoLoginPage();
    }


}
