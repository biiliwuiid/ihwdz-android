package com.ihwdz.android.hwapp.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.base.listener.LoginListenerHandler;
import com.ihwdz.android.hwapp.base.listener.OnLoginSuccessListener;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.model.entity.TabEntity;
import com.ihwdz.android.hwapp.ui.main.advertisement.AdvertisementActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import com.ihwdz.android.hwslimcore.Settings.IAppSettings;
import com.ihwdz.android.hwslimcore.Settings.SlimAppSettings;

import java.util.ArrayList;

import javax.inject.Inject;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/7/24.
 * desc : 首页
 * version: 1.0
 * </pre>
 */
public class MainPresenter implements MainContract.Presenter {

    String TAG = "MainPresenter";
    @Inject MainActivity parentActivity;
    @Inject MainContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    OnLoginSuccessListener mLoginListener;

    LoginDataModel model = new LoginDataModel(parentActivity);

    IAppSettings settings;


    @Inject
    public MainPresenter(MainActivity activity) {
        this.parentActivity = activity;
        settings = new SlimAppSettings(activity);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

        mSubscriptions.unsubscribe();
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
    public ArrayList<CustomTabEntity> getTabEntity() {
        ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
        TypedArray mIconUnSelectIds = parentActivity.getResources().obtainTypedArray(R.array.main_tab_un_select);
        TypedArray mIconSelectIds = parentActivity.getResources().obtainTypedArray(R.array.main_tab_select);
        String[] mainTitles = parentActivity.getResources().getStringArray(R.array.main_title);
        for (int i = 0; i < mainTitles.length; i++) {
            int unSelectId = mIconUnSelectIds.getResourceId(i, R.drawable.tab_homepage);
            int selectId = mIconSelectIds.getResourceId(i, R.drawable.tab_homepage_selected);
            mTabEntities.add(new TabEntity(mainTitles[i],selectId , unSelectId));
        }
        mIconUnSelectIds.recycle();
        mIconSelectIds.recycle();
        return mTabEntities;
    }

    // TODO: 2018/7/24  update version
    @Override
    public void getUpdate() {

    }

    @Override
    public boolean getIsFirstLoad() {
        return Constant.FIRST_LOAD;
    }

    @Override
    public void setIsFirstLoad(boolean isFirstLoad) {
        Constant.FIRST_LOAD = isFirstLoad;
    }

    @Override
    public void goAdvertisement() {
        Intent intent = new Intent(parentActivity.getBaseContext(), AdvertisementActivity.class);
        parentActivity.startActivity(intent);
    }

    @Override
    public boolean getIsLogout() {
        Constant.LOGOUT = settings.getIsLogout();
        return Constant.LOGOUT;
    }

    @Override
    public void setIsLogout(boolean isLogout) {
        Constant.LOGOUT = isLogout;
        settings.storeIsLogout(Constant.LOGOUT);
    }

    @Override
    public void login() {
        LogUtils.printInfo("login", "LOGIN =============================== by password");
        Constant.user_account = settings.getLastUserName();
        Constant.user_pwd = settings.getLastUserPwd();
        LogUtils.printInfo(TAG, "user_account: " +  Constant.user_account);
        LogUtils.printInfo(TAG, "user_pwd: " +  Constant.user_pwd);

        if(!TextUtils.isEmpty(Constant.user_account) && Constant.user_account.length() > 0 ){
            Subscription rxSubscription = model
                    .postLoginData(Constant.user_account, Constant.user_pwd)
                    .compose(RxUtil.<LoginData>rxSchedulerHelper())
                    .subscribe(new Subscriber<LoginData>() {
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
                        public void onNext(LoginData data) {
                            Log.d(TAG, "onNext");

                            if ("0".equals(data.code)){

                                if (data.data != null){

                                    Constant.LOGOUT = false;
                                    Constant.token = data.data.token;  // 根据拿到的 token 去获取用户类型 进而加载不同用户界面（infoVip/dealVip/business）
                                    Log.d(TAG, "token: " + Constant.token);
                                    Constant.user_account = data.data.accountNo;
                                    Constant.expireTime = data.data.expireTime; // token 有效期

                                    settings.storeToken(Constant.token);   // 保存TOKEN
                                    settings.storeUserNameString(Constant.user_account);
                                    settings.storeUserPwd(Constant.user_pwd);
                                    settings.storeIsLogout(false);
                                    getUserType();
                                }
                            }else {
                                // 登录已过期请重新登录
                                mView.showPromptMessage(parentActivity.getResources().getString(R.string.login_state_expired));
                                // mView.showPromptMessage(data.getMsg());
                            }
                        }
                    });
            mSubscriptions.add(rxSubscription);

        }else {
            LogUtils.printCloseableInfo(TAG, "user account is null");
        }

    }

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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserTypeData data) {
                        if ("0".equals(data.code)){
                            if (data != null && data.data != null){

                                Constant.VIP_TYPE = data.data.type;                //会员类型：-1 用户; 0 资讯; 1 交易; 2 商家;
                                Constant.VIP_LOCK_STATUS = data.data.status;      // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                                Constant.VIP_AUTHENTIC = data.data.authStatus == 1;   // 认证状态 0==未认证, 1==已认证

                                //settings.storeUserType(data.data);   // 保存用户类型 (保存账号密码 重新登录，重新获取用户类型、状态等信息)
                                getUserStatus();

                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserStateData data) {
                        if ("0".equals(data.code)){
                            // Constant.APPLY_STATUS = Integer.parseInt(data.data.type);
                            // 开通交易会员结果 //开通交易会员结果 : 0未申请 1 申请中 2申请失败
                            Constant.APPLY_STATUS = Integer.parseInt(data.data.tradeMemberApplyStatus);

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

                            /**
                             * 登录成功！
                             */
                            try{
                                LogUtils.printCloseableInfo("login","LoginListenerHandler ====================== MainPresenter");
                                LoginListenerHandler.getLoginSuccessListener().onLoginSuccess();
                            }catch (Exception e){
                                LogUtils.printError(TAG, e.toString());
                            }


                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
                                mView.showPromptMessage(data.msg);
                            }
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    // 2018/7/24  CALL_PHONE,CAMERA now no use
    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void startPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            //具备权限 直接进行操作
            //Toast.makeText(this, "Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限

            EasyPermissions.requestPermissions(
                    parentActivity,
                    parentActivity.getResources().getString(R.string.easy_permissions),
                    RC_LOCATION_CONTACTS_PERM,
                    LOCATION_AND_CONTACTS);
        }
    }

    /**
     * 判断是否添加了权限
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(parentActivity, LOCATION_AND_CONTACTS);
    }
}
