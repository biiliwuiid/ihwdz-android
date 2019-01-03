package com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist;

import android.os.Bundle;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.DealRecordData;
import com.ihwdz.android.hwapp.model.bean.LoginData;
import com.ihwdz.android.hwapp.model.bean.ProductData;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;
import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist.PurchaseListContract.PageSize;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :
 * version: 1.0
 * </pre>
 */
public class PurchaseListPresenter implements PurchaseListContract.Presenter {

    String TAG = "PurchaseListPresenter";
    @Inject PurchaseListActivity parentActivity;
    @Inject PurchaseListContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    LoginDataModel model;

    @Inject PurchaseListAdapter mAdapter;

    int currentPageNum = 1;

    @Inject
    public PurchaseListPresenter(PurchaseListActivity activity){
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
    public void refreshData() {

    }

    @Override
    public PurchaseListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void getPurchaseData() {

        Log.d(TAG, "LOGIN by password");
//        Subscription rxSubscription = model
//                .getPurchaseData(Constant.token, currentPageNum, PageSize)
//                .compose(RxUtil.<DealRecordData>rxSchedulerHelper())
//                .subscribe(new Subscriber<DealRecordData>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d(TAG, "onCompleted");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, "onError");
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(DealRecordData data) {
//                        Log.d(TAG, "onNext");
//
//                        if ("0".equals(data.getCode())){
//
//                            if (data.getData() != null){
//
//                                Constant.token = data.getData().getToken();// 根据拿到的 token 去获取用户类型 进而加载不同用户界面（infoVip/dealVip/business）
//                                Log.d(TAG, "token: " + Constant.token);
//                                Constant.user_account = data.getData().getAccountNo();
//                                Constant.expireTime = data.getData().getExpireTime();
//                            }
//                        }else {
//                            mView.showPromptMessage(data.getMsg());
//                        }
//                    }
//                });
//        mSubscriptions.add(rxSubscription);
    }
}
