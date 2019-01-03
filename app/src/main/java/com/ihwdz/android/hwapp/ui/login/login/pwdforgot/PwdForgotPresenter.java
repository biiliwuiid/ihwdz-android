package com.ihwdz.android.hwapp.ui.login.login.pwdforgot;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.ui.login.login.LoginActivity;
import com.ihwdz.android.hwapp.ui.login.login.LoginContract;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/15
 * desc :
 * version: 1.0
 * </pre>
 */
public class PwdForgotPresenter implements PwdForgotContract.Presenter {

    String TAG = "PwdForgotPresenter";

    @Inject PwdForgotActivity parentActivity;
    @Inject PwdForgotContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    LoginDataModel model = new LoginDataModel(parentActivity);
    private String accountNo;
    private String password;
    private String checkCode;

    @Inject
    public PwdForgotPresenter(PwdForgotActivity activity){
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
    public void sendCheckCode() {
        Log.d(TAG, "update password : -------- sendCheckCode");
        Subscription rxSubscription = model
                .getPwdUpdateVerifyCode(accountNo)
                .compose(RxUtil.<UserData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserData data) {
                        Log.d(TAG, "onNext");

                        if ("0".equals(data.getCode())){

                            if (data.getData() != null){

                            }
                        }else {
                            mView.showPromptMessage(data.getMsg());
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setAccount(String account) {
        this.accountNo = account;

        boolean isPhoneNum = isMobileNO(accountNo);
        if (TextUtils.isEmpty(accountNo)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_no_null));
            mView.setSendCodeClickable(false);
            return;
        }else if (!isPhoneNum){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_not_right));
            mView.setSendCodeClickable(false);
            return;
        }else {
            mView.setSendCodeClickable(true);

            sendCheckCode();
        }

    }

    @Override
    public void setPwd(String pwd) {
        this.password = pwd;
    }

    @Override
    public void setCheckCode(String code) {
        this.checkCode = code;
        if (TextUtils.isEmpty(checkCode)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.verification_code_null));
            return;
        }

    }

    @Override
    public void updatePassword() {
        Log.d(TAG, "update password ");
        Subscription rxSubscription = model
                .updatePwd(accountNo, password, checkCode)
                .compose(RxUtil.<UserData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserData data) {
                        Log.d(TAG, "onNext");

                        if ("0".equals(data.getCode())){
                            mView.showPromptMessage(data.getMsg());
                            mView.showLoginView();
                            if (data.getData() != null){
                            }
                        }else {
                            mView.showPromptMessage(data.getMsg());
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
     * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

}
