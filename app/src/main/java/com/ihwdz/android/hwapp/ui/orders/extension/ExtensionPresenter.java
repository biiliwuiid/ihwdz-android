package com.ihwdz.android.hwapp.ui.orders.extension;

import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.ExtensionData;
import com.ihwdz.android.hwapp.model.bean.OrdersData;
import com.ihwdz.android.hwapp.model.bean.VerifyData;
import com.ihwdz.android.hwapp.ui.orders.OrderItemAdapter;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailActivity;
import com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract;
import com.ihwdz.android.hwapp.ui.purchase.reviewquote.ReviewActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import cn.qqtheme.framework.picker.NumberPicker;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.common.Constant.Remarks.EXTENSION_APPLY;
import static com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract.MODE_HIDE;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/28
 * desc :  申请展期  设置提交按钮 提交成功后不可再次点击
 * version: 1.0
 * </pre>
 */
public class ExtensionPresenter implements ExtensionContract.Presenter {

    String TAG = "OrderDetailPresenter";

    @Inject ExtensionActivity parentActivity;
    @Inject ExtensionContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    private boolean isSubmitClicked = false;

    private String currentOrderId;
    private int paymentDayMax = 30;         // 最大账期
    private double creditAmount = 0.00d;    // 授信额度
    private double extensionRate = 0.00d;   // 展期利率

    // 展期 post
    private long orderId = 0L;              // 订单Id  orderId = Long.valueOf(currentOrderId);
    private String contractCode = "";       // 账单号
    private double extensionAmount = 0D;    // 展期金额 - need calculate
    private int currentPaymentDay = 0;      // 账期     - need select
    private String currentRemarks = "";     // 备注     - need edit

    // 初始化 展期 数据
    private void initPostData(ExtensionData.ExtensionEntity data) {
        this.orderId = Long.valueOf(currentOrderId);
        this.contractCode = data.contractCode;
        // 授信额度
        if (data.creditAmount != null && data.creditAmount.length() > 0){
            this.creditAmount = Double.valueOf(data.creditAmount);
            creditAmount = (double) Math.round(creditAmount * 100) / 100;
        }
        // 展期利率
        if (data.extendsDaysRate != null && data.extendsDaysRate.length() > 0){
            this.extensionRate = Double.valueOf(data.extendsDaysRate);
        }
        // 账期
        currentPaymentDay = paymentDayMax;

    }

    @Inject
    public ExtensionPresenter (ExtensionActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);
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
    public void setCurrentId(String id) {
        this.currentOrderId = id;
    }

    @Override
    public void getExtensionData() {
        Subscription rxSubscription = model
                .getExtensionData(currentOrderId)
                .compose(RxUtil.<ExtensionData>rxSchedulerHelper())
                .subscribe(new Subscriber<ExtensionData>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                        LogUtils.printError(TAG, "getExtensionData: onError : " + e.toString());
                    }

                    @Override
                    public void onNext(ExtensionData data) {

                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                initPostData(data.data);
                                mView.updateView(data.data);
                            }
                        }else {
                            LogUtils.printError(TAG, "getExtensionData: onNext : " + data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }


    @Override
    public int getMaxPaymentDay() {
        return paymentDayMax;
    }

    // 展期金额
    @Override
    public double getExtensionAmount() {
        extensionAmount = creditAmount * (extensionRate/100) * currentPaymentDay;
        extensionAmount = (double) Math.round(extensionAmount * 100) / 100;  // 上传时也是四舍五入保留 2位小数
        LogUtils.printCloseableInfo(TAG, "creditAmount: "+creditAmount);
        LogUtils.printCloseableInfo(TAG, "extensionRate: "+extensionRate);
        LogUtils.printCloseableInfo(TAG, "currentPaymentDay: "+currentPaymentDay);
        LogUtils.printCloseableInfo(TAG, "extensionAmount: "+extensionAmount);
        return extensionAmount;
    }

    @Override
    public void setCurrentRemarks(String remarks) {
        this.currentRemarks = remarks;
    }

    // 选择账期
    @Override
    public void doSelectPaymentDay() {
        // mView.showPromptMessage("底部弹框 账期");

        NumberPicker picker = new NumberPicker(parentActivity);
        picker.setCycleDisable(true);
        picker.setDividerVisible(false);
        picker.setOffset(2);                               //偏移量
        picker.setRange(1, paymentDayMax, 1);//数字范围
        picker.setSelectedItem(paymentDayMax);
        picker.setOnNumberPickListener(new NumberPicker.OnNumberPickListener() {
            @Override
            public void onNumberPicked(int index, Number item) {
                currentPaymentDay  = item.intValue();
                mView.updatePaymentDay(currentPaymentDay);

            }
        });
        picker.show();
    }

    @Override
    public void doRemarks() {
        // mView.showPromptMessage("备注");
        boolean isHint = true;
        if (currentRemarks != null && currentRemarks.length() > 0){
            isHint = false;
        }
        ReviewActivity.startReviewActivity(parentActivity, EXTENSION_APPLY, currentRemarks,isHint, null, null);

    }

    @Override
    public boolean getIsSubmitClicked() {
        return isSubmitClicked;
    }

    //2018/12/6  申请展期 -提交申请
    @Override
    public void doSubmit() {
        // mView.showPromptMessage("申请展期");
        isSubmitClicked = true;
        Subscription rxSubscription = model
                .postExtensionData(orderId, contractCode, extensionAmount, currentPaymentDay, currentRemarks)
                .compose(RxUtil.<VerifyData>rxSchedulerHelper())
                .subscribe(new Subscriber<VerifyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "post 申请展期 onError: " + e.toString());
                        mView.showPromptMessage(e.toString());
                        isSubmitClicked = false;
                    }

                    @Override
                    public void onNext(VerifyData data) {

                        if (TextUtils.equals("0", data.code)){
                            mView.showPromptMessage(data.msg);
                            goBack();

                        }else {
                            mView.showPromptMessage(data.msg);
                            isSubmitClicked = false;
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    @Override
    public void goBack() {
        Constant.orderOptionDone = true; // 按钮操作完成
        parentActivity.onBackClicked();
    }


}
