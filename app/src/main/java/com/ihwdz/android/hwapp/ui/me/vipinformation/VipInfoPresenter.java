package com.ihwdz.android.hwapp.ui.me.vipinformation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserInformation;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.orders.filter.CityFilterActivity;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/12
 * desc :   会员信息  只展示信息 不修改
 * version: 1.0
 * </pre>
 */
public class VipInfoPresenter implements VipInfoContract.Presenter {

    String TAG = "VipInfoPresenter";
    @Inject VipInfoActivity parentActivity;
    @Inject VipInfoContract.View mView;
    @Inject CompositeSubscription mSubscriptions;

    LoginDataModel model;
    String token;

    String name,email,mobile,
            provinceCode,province,
            cityCode,city,
            districtCode,district, address;
    boolean isUpdate = false;   // 是否修改信息

    @Inject
    VipInfoPresenter(VipInfoActivity activity){
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
    public void getVipInfo() {
        Log.d(TAG, "getVipInfo ================ token:"+ Constant.token);
        Subscription rxSubscription = model
                .getVipInfo(Constant.token)
                .compose(RxUtil.<UserInformation>rxSchedulerHelper())
                .subscribe(new Subscriber<UserInformation>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "------- onError ---------" + e.toString());
                    }

                    @Override
                    public void onNext(UserInformation data) {
                        Log.d(TAG, "------- onNext ---------");

                        if ("0".equals(data.code)){
                            mView.showVipInfo(data.data);
                        }
                    }


                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void goSelectDistrict() {

//        Intent intent = new Intent(parentActivity.getBaseContext(), CityFilterActivity.class);
//        intent.putExtra("VIP_DISTRICT",true);
//        parentActivity.startActivity(intent);
    }

    @Override
    public void postUserUpdate() {
        Log.d(TAG, "postUserUpdate =========================== token:" + Constant.token);
        updateVipInfo();
        Subscription rxSubscription = model
                .postUserData(
                        name,email,mobile,
                        provinceCode,province,
                        cityCode,city,
                        districtCode,district, address
                        //companyNature, companyType, companyFullName
                )
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "------- onCompleted ---------");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "------- onError ---------" + e.toString());
                    }

                    @Override
                    public void onNext(VerifyData data) {
                        Log.d(TAG, "------- onNext ---------");
                        Log.d(TAG, "------- data: " + data.msg);
                        if ("0".equals(data.code)) {
                            if (data.data){
                                mView.showPromptMessage(data.msg);

                            }
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }

                });
        mSubscriptions.add(rxSubscription);
    }

    public void updateVipInfo(){
        name = Constant.name;
        mobile = Constant.tel;
        email = Constant.email;
        province = Constant.provinceName;
        provinceCode = Constant.provinceCode;
        city = Constant.cityName;
        cityCode = Constant.cityCode;
        district = Constant.districtName;
        districtCode = Constant.districtCode;
        address = Constant.address;
    }

}
