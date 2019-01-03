package com.ihwdz.android.hwapp.ui.login.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.listener.LoginListenerHandler;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.UserData;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.login.login.LoginContract.MODEL_PWD;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/05
 * desc :   登录 1.账号密码登录; 2.验证码登录; 3. 忘记密码;
 * version: 1.0
 * </pre>
 */
public class LoginPresenter implements LoginContract.Presenter{

    String TAG = "LoginPresenter";

    @Inject LoginActivity parentActivity;
    @Inject LoginContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    LoginDataModel model;

    IAppSettings settings;

    private String userName;
    private String companyName;
    private String telephoneNo;
    private long verificationCode;
    private String pwd;

    private int currentLoginModel = MODEL_PWD;  // 默认: 账号密码登录模式
    private String accountNo;
    private String password;
    private String checkCode;

    private String token;

    private  boolean mIsFromPersonalCenter = false;

    @Inject
    public LoginPresenter(LoginActivity activity){
        this.parentActivity = activity;
        model = LoginDataModel.getInstance(parentActivity);
        settings = SlimAppSettings.getInstance(parentActivity);
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
    public void setCurrentLoginModel(int model) {
        if (currentLoginModel != model){
            this.currentLoginModel = model;
            mView.updateView();
        }
    }

    @Override
    public int getCurrentLoginModel() {
        return this.currentLoginModel;
    }

    @Override
    public void setIsFromPersonalCenter(boolean is) {
        mIsFromPersonalCenter = is;
    }

    @Override
    public boolean isFromPersonalCenter() {
        return mIsFromPersonalCenter;
    }


    // 账号密码登录
    @Override
    public void login() {

        Log.d(TAG, "LOGIN by password");
        Subscription rxSubscription = model
                .postLoginData(accountNo, password)
                .compose(RxUtil.<LoginData>rxSchedulerHelper())
                .subscribe(new Subscriber<LoginData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "login onError: " + e.toString());
                        mView.showPromptMessage(e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(LoginData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){
                                Constant.LOGOUT = false;
                                Constant.token = data.data.token;  // 根据拿到的 token 去获取用户类型 进而加载不同用户界面（infoVip/dealVip/business）
                                Constant.user_account = data.data.accountNo;
                                Constant.user_pwd = password;
                                Constant.expireTime = data.data.expireTime; // token 有效期

                                settings.storeToken(Constant.token);   // 保存TOKEN
                                settings.storeUserNameString(Constant.user_account);
                                settings.storeUserPwd(Constant.user_pwd);
                                settings.storeIsLogout(false);
                                getUserType();

                            }
                        }else {

                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    // 用户类型、认证、锁定状态 (登录之后获取)
    @Override
    public void getUserType() {
        Subscription rxSubscription = model
                .getUserType()
                .compose(RxUtil.<UserTypeData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserTypeData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getUserType onError: " + e.toString());
                        mView.showPromptMessage("" + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserTypeData data) {
                        if ("0".equals(data.code)){
                            if (data != null && data.data != null){

                                Constant.VIP_TYPE = data.data.type;                   // 会员类型：-1 用户; 0 资讯; 1 交易; 2 商家;
                                Constant.VIP_LOCK_STATUS = data.data.status;          // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                                Constant.VIP_AUTHENTIC = data.data.authStatus == 1;   // 认证状态 0==未认证, 1==已认证

                                //settings.storeUserType(data.data);   // 保存用户类型 (保存账号密码 重新登录，重新获取用户类型、状态等信息)
                                getUserStatus();

                            }else {
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);

    }


    //
    @Override
    public void getUserStatus() {
        Subscription rxSubscription = model
                .getUserStatus()
                .compose(RxUtil.<UserStateData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserStateData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "getUserStatus onError: " + e.toString());
                        mView.showPromptMessage("" + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserStateData data) {
                        if ("0".equals(data.code)){

                            // Constant.APPLY_STATUS = Integer.parseInt(data.data.type);
                            //开通交易会员结果 : 0未申请 1 申请中 2申请失败  3完善资料
                            Constant.APPLY_STATUS = Integer.parseInt(data.data.tradeMemberApplyStatus);
                            LogUtils.printInfo(TAG, "tradeMemberApplyStatus: " + data.data.tradeMemberApplyStatus );
                            LogUtils.printInfo(TAG, "Constant.APPLY_STATUS: " + Constant.APPLY_STATUS );

                            // 交易会员是否通过认证
                            boolean isAuthenticated = false;
                            if (data.data.totalAmount != null && data.data.totalAmount.length()> 0){
                                isAuthenticated = true;
                                Constant.totalAmount = data.data.totalAmount;
                                Constant.usedAmount = data.data.usedAmount;
                                Constant.availableAmount = data.data.availableAmount;
                            }

                            Constant.isAuthenticated = isAuthenticated;



                            Constant.goodsName = data.data.goodsName;
                            Constant.endDateStr = data.data.endDateStr;
                            Constant.endDate = data.data.goodsName + data.data.endDateStr;
//                            settings.storeEndDate(Constant.endDate);
//                            settings.storeApplyStatus(data.data.tradeMemberApplyStatus);
//                            settings.storeIsAuthenticated(isAuthenticated);

                            try{
                                LogUtils.printCloseableInfo("login","LoginListenerHandler ====================== LoginPresenter");
                                LoginListenerHandler.getLoginSuccessListener().onLoginSuccess();
                            }catch (Exception e){
                                LogUtils.printError(TAG, e.toString());
                            }

                            if (mIsFromPersonalCenter){
                                mView.showPersonalCenter(Constant.VIP_TYPE); // param is needn't
                            }else {
                                //  2019/1/3   从哪里登录 回哪里
                                parentActivity.onBackClicked();
                            }



                        }else {
                            mView.showPromptMessage(data.msg);
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void sendCheckCode() {
        Subscription rxSubscription = model
                .getLoginVerifyCode(accountNo)
                .compose(RxUtil.<UserData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "sendCheckCode onError: " + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserData data) {
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

    // 动态密码登录
    @Override
    public void loginByCheckCode() {
        Subscription rxSubscription = model
                .postLoginByCodeData(accountNo, checkCode)
                .compose(RxUtil.<LoginData>rxSchedulerHelper())
                .subscribe(new Subscriber<LoginData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "loginByCheckCode onError: " + e.toString());
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());
                    }

                    @Override
                    public void onNext(LoginData data) {
                        if ("0".equals(data.code)){
                            if (data.data != null){

                                Constant.LOGOUT = false;
                                Constant.token = data.data.token;  // 根据拿到的 token 去获取用户类型 进而加载不同用户界面（infoVip/dealVip/business）
                                Constant.user_account = data.data.accountNo;
                                Constant.user_pwd = password;
                                Constant.expireTime = data.data.expireTime; // token 有效期

                                //settings.storeToken(Constant.token);   // 动态密码登录 不保存TOKEN
                                //settings.storeUserNameString(Constant.user_account);
                                getUserType();
                            }
                        }else {
                            mView.showPromptMessage(data.msg);
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
}
