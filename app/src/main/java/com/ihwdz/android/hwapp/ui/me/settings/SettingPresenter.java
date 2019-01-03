package com.ihwdz.android.hwapp.ui.me.settings;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/18
 * desc :
 * version: 1.0
 * </pre>
 */
public class SettingPresenter implements SettingContract.Presenter {

    String TAG = "LoginPresenter";
    @Inject SettingsActivity parentActivity;
    @Inject SettingContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    LoginDataModel model;
    String token;

    IAppSettings settings;

    @Inject
    public SettingPresenter(SettingsActivity activity){
        this.parentActivity = activity;
        settings = new SlimAppSettings(activity);
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

    // 退出登录 -》 个人中心

    @Override
    public void logout() {
        Log.d(TAG, "token:"+ Constant.token);
        Subscription rxSubscription = model
                .doLogout(Constant.token)
                .compose(RxUtil.<UserData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "------- onError ---------");
                    }

                    @Override
                    public void onNext(UserData data) {
                        Log.d(TAG, "------- onNext ---------");
                        Log.d(TAG, "------- data: "+data.getMsg());

                        if ("0".equals(data.getCode())){
                            // 成功退出
                            Constant.LOGOUT = true;
                            Constant.token = "";
                            Constant.user_account = parentActivity.getResources().getString(R.string.login_per);
                            Constant.user_pwd = null;
                            Constant.VIP_TYPE = 100;    // 未登录

                            settings.storeToken(null);   // 保存TOKEN
                            settings.storeUserNameString(null);
                            settings.storeUserPwd(null);
                            settings.storeIsLogout(true);
                            mView.showPromptMessage(data.msg);
                            mView.showPersonalCenter();
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }


                });
        mSubscriptions.add(rxSubscription);
    }
}
