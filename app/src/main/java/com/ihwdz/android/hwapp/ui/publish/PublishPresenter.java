package com.ihwdz.android.hwapp.ui.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserTypeData;
import com.ihwdz.android.hwapp.ui.login.LoginPageActivity;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.ApplyProgressActivity;
import com.ihwdz.android.hwapp.ui.publish.purchase.PurchaseActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/04
 * desc :
 * version: 1.0
 * </pre>
 */
public class PublishPresenter implements PublishContract.Presenter{


    String TAG = "PublishPresenter";

    private MainActivity activity;
    private PublishContract.View mView;
    @NonNull private CompositeSubscription mSubscriptions;
    LogisticsModel model;

    public PublishPresenter(PublishContract.View view) {
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
        model = LogisticsModel.getInstance(activity);
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

                                Constant.VIP_TYPE = data.data.type;               //会员类型：-1 用户; 0 资讯; 1 交易; 2 商家;
                                Constant.VIP_LOCK_STATUS = data.data.status;      // 会员锁定 状态： 0 -正常, 1 -锁定, 2 -注销
                                Constant.VIP_AUTHENTIC = data.data.authStatus == 1;   // 认证状态 0==未认证, 1==已认证

                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
//                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
//                                if (Constant.token != null && Constant.token.length()>0){
//                                    mView.showPromptMessage(data.msg);
//                                }
//
//                            }
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void gotoLoginPage() {
        LoginPageActivity.startLoginPageActivity(activity);
    }

    @Override
    public void gotoApplyDealVip() {

        ApplyProgressActivity.startApplyProgressActivity(activity, Constant.ApplyFrom.PUBLISH_PURCHASE);
    }

    @Override
    public void gotoPublishPurchase() {
        LogUtils.printCloseableInfo(TAG, "goto Publish Purchase");
        Intent intent = new Intent(activity, PurchaseActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoPurchasePool() {

        MainActivity.startPurchasePool(activity, 1,true);
    }
}
