package com.ihwdz.android.hwapp.ui.me.vipinformation.updatepic;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.LicenseData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import java.io.File;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/17
 * desc :
 * version: 1.0
 * </pre>
 */
public class PicUpdatePresenter implements PicUpdateContract.Presenter {

    String TAG = "PicUpdatePresenter";
    @Inject PicUpdateActivity parentActivity;
    @Inject PicUpdateContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    private String licensePath;

    @Inject public PicUpdatePresenter(PicUpdateActivity activity){
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
    public Uri getUri(String path){
        File file = new File(path);
        return Uri.fromFile(file);
    }

    @Override
    public void setLicensePath(String path) {
        this.licensePath = path;
    }

    @Override
    public boolean isLicenseUpload() {
        if (TextUtils.isEmpty(licensePath)){
            mView.showPromptMessage(parentActivity.getResources().getString(R.string.license_null));
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void postLicense(String token, String filePath) {
        Subscription rxSubscription = model
                .postLicense(Constant.token, filePath)
                .subscribe(new Subscriber<LicenseData>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.printCloseableInfo(TAG, "=========   onCompleted   =========== " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printCloseableInfo(TAG, "=========   onError   =========== " + e.toString());
                    }

                    @Override
                    public void onNext(LicenseData data) {
                        LogUtils.printCloseableInfo(TAG, "=========   onNext   =========== " + data.data.legalPerson);

                        if ("0".equals(data.code)){
                            mView.showLicenseInfo(data.data);
                        }else {
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });



        mSubscriptions.add(rxSubscription);
    }
}
