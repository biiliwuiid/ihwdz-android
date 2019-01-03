package com.ihwdz.android.hwapp.ui.login.register;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.login.eula.EulaActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/06
 * desc :
 * version: 1.0
 * </pre>
 */
public class RegisterPresenter implements RegisterContract.Presenter {

    String TAG = "RegisterPresenter";

    @Inject RegisterActivity parentActivity;
    @Inject RegisterContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    private String userName;     // 用户名
    private String telephoneNo;  // 手机号（账号）
    private String checkNum;     // 验证码
    private String pwd;          // 密码

    private boolean isEulaAccepted; // 是否同意用户协议

    @Inject
    public RegisterPresenter(RegisterActivity activity){
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
    public void checkIsRegistered() {
        Log.d(TAG, "checkIsRegistered ============== telephone: " + telephoneNo);
        Subscription rxSubscription = model
                .getIsRegistered(telephoneNo)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "checkIsRegistered ============== onCompleted");
                        //mView.hideKeyboard();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "checkIsRegistered ============== onError: "+e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        Log.d(TAG, "checkIsRegistered ============== onNext");
                        if (data != null && "0".equals(data.code)){
                            if (data.data){
                                // 未注册
                                sendVerificationCode();
                                mView.setSendCodeClickable(true);  // 开始倒计时
                            }else {
                                // 已注册
                                mView.showAccountExisted();

                            }
                        }else {
                            mView.showErrorView("data == null");
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    /**
     * 获取验证码  注册 type = 0
     */
    @Override
    public void sendVerificationCode() {
        Subscription rxSubscription = model
                .getRegisterVerifyCode(telephoneNo)
                .compose(RxUtil.<UserData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "getVerificationCode ============== onCompleted");
                        //mView.hideKeyboard();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "getVerificationCode ============== onError: "+ e.toString());
                    }

                    @Override
                    public void onNext(UserData data) {
                        Log.d(TAG, "getVerificationCode ============== onNext: ");
                        if (data != null){
                            if ("0".equals(data.getCode())){
                                 //验证码获取成功
                                mView.showErrorView(data.getMsg());
                            }else {
                                // 验证码获取失败
                                mView.showErrorView(data.getMsg());
                            }
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void postRegisterData() {
        Subscription rxSubscription = model
                //.postRegisterData(mUserEntity)
                .postRegisterData(telephoneNo, pwd,userName,checkNum)
                .compose(RxUtil.<LoginData>rxSchedulerHelper())
                .subscribe(new Subscriber<LoginData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "postRegisterData ============== onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //mView.showErrorView(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(LoginData data) {
                        if (data != null){
                            if ("0".equals(data.code)){
                                // 注册成功
//                                int role = data.getData().getMemberRole();
//                                Constant.VIP_TYPE = role;
                                Constant.token = data.data.token;
                                Constant.user_account = data.data.accountNo;
                                mView.showRegisterSuccess(data.msg);

                            }else {
                                mView.showErrorView(data.msg);
                            }
                        }else {
                            mView.showErrorView("data = null");
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);


    }

    @Override
    public void checkTermsOfService() {
        String title = parentActivity.getResources().getString(R.string.title_terms_of_service);
        EulaActivity.startEulaActivity(parentActivity.getBaseContext(), title,0);
    }

    @Override
    public void checkPrivacyPolicy() {
        String title = parentActivity.getResources().getString(R.string.title_privacy_policy);
        EulaActivity.startEulaActivity(parentActivity.getBaseContext(),title, 1);
    }

    @Override
    public void setUserName(String name) {
        this.userName = name;
        if (TextUtils.isEmpty(userName)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.name_null));
            return;
        }
    }

    @Override
    public void setTelephone(String tel) {
        this.telephoneNo = tel;
        Log.d(TAG, " TEL: " + telephoneNo);

        boolean isPhoneNum = isMobileNO(telephoneNo);
        if (TextUtils.isEmpty(telephoneNo)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_no_null));
            mView.setSendCodeClickable(false);
            return;
        }else if (!isPhoneNum){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.telephone_not_right));
            mView.setSendCodeClickable(false);
            return;
        }else {
            checkIsRegistered();   // 校验账号是否存在
        }
    }

    @Override
    public void setCheckCode(String code) {
        this.checkNum = code;
        Log.d(TAG, " checkNum: " + checkNum);
        if (TextUtils.isEmpty(checkNum)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.verification_code_null));
            return;
        }
    }

    @Override
    public void setPassword(String password) {

        this.pwd = password;
        if (TextUtils.isEmpty(pwd)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.pwd_null));
            return;
        }
    }

    @Override
    public void setIsEulaAccepted(boolean value) {
        if (isEulaAccepted != value){
            this.isEulaAccepted = value;
            if (!isEulaAccepted){
                mView.showPromptMessage(parentActivity.getResources().getString(R.string.eula_null));
            }
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

}
