package com.ihwdz.android.hwapp.ui.me.dealvip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.UserStateData;
import com.ihwdz.android.hwapp.ui.main.MainActivity;
import com.ihwdz.android.hwapp.ui.me.collections.CollectionsActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.apply.QuotaActivity;
import com.ihwdz.android.hwapp.ui.me.dealvip.deposit.DepositActivity;
import com.ihwdz.android.hwapp.ui.me.improveinfo.ImproveInfoActivity;
import com.ihwdz.android.hwapp.ui.me.messages.MessagesActivity;
import com.ihwdz.android.hwapp.ui.me.settings.SettingsActivity;
import com.ihwdz.android.hwapp.ui.me.vipinformation.VipInfoActivity;
import com.ihwdz.android.hwapp.ui.orders.logistics.LogisticActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.loginData.LoginDataModel;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/17
 * desc :   交易会员 会员中心
 * version: 1.0
 * </pre>
 */
public class DealVipPresenter implements DealVipContract.Presenter {

    String TAG = "DealVipPresenter";

    private MainActivity activity;
    private DealVipContract.View mView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    LoginDataModel model;

    private String deposit = null;        // 保证金
    private String memberServeId = null;

    public DealVipPresenter(DealVipContract.View view) {
        this.mView = view;
        mSubscriptions = new CompositeSubscription();
        model = LoginDataModel.getInstance(activity);
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
    public void getApplyStateData() {

        Subscription rxSubscription = model
                .getUserStatus()
                .compose(RxUtil.<UserStateData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserStateData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserStateData data) {
                        if ("0".equals(data.code)){
                            // Constant.APPLY_STATUS = Integer.parseInt(data.data.type);
                            // 开通交易会员结果 : 0未申请 1 申请中 2申请失败 3完善资料
                            Constant.APPLY_STATUS = Integer.parseInt(data.data.tradeMemberApplyStatus);

                            // 交易会员是否通过认证
                            boolean isAuthenticated = false;
                            if (data.data.totalAmount != null && data.data.totalAmount.length()> 0){
                                isAuthenticated = true;

                            }
                            Constant.isAuthenticated = isAuthenticated;
                            Constant.totalAmount = data.data.totalAmount;
                            Constant.usedAmount = data.data.usedAmount;
                            Constant.availableAmount = data.data.availableAmount;

                            // TODO: 2018/12/18 保证金
                            Constant.depositAmount = data.data.deposit;
                            deposit = data.data.deposit;
                            memberServeId = data.data.memberServeId;
                            mView.updateView(data.data.totalAmount, data.data.deposit);

                            Constant.goodsName = data.data.goodsName;
                            Constant.endDateStr = data.data.endDateStr;
                            Constant.endDate = data.data.goodsName + data.data.endDateStr;


                        }else {
                            LogUtils.printCloseableInfo(TAG,"data.msg: ======================= "+ data.msg);
//                            if (!TextUtils.isEmpty(data.msg) && data.msg.length() > 0){
//                                mView.showPromptMessage(data.msg);
//                            }
                        }


                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void gotoCollections() {
        CollectionsActivity.startCollectionsActivity(activity.getBaseContext());
    }

    @Override
    public void gotoMsgCenter() {
        Intent intent = new Intent(activity.getBaseContext(), MessagesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoUpdateUserInfo() {
        Intent intent = new Intent(activity.getBaseContext(), ImproveInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoMyVipInfo() {
        Intent intent = new Intent(activity.getBaseContext(),VipInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoCheckQuota() {
        Intent intent = new Intent(activity.getBaseContext(),QuotaActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoDeposit() {
        DepositActivity.startDepositActivity(activity, deposit, memberServeId, 0);
    }

    @Override
    public void gotoLogistics() {
        Intent intent = new Intent(activity, LogisticActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void gotoSettings() {
        SettingsActivity.startSettingsActivity(activity.getBaseContext(),1);
    }


}
