package com.ihwdz.android.hwapp.ui.orders.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;

import com.ihwdz.android.hwapp.R;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrderDetailData;
import com.ihwdz.android.hwapp.ui.me.records.invoice.InvoiceActivity;
import com.ihwdz.android.hwapp.ui.orders.OrderItemAdapter;
import com.ihwdz.android.hwapp.ui.orders.extension.ExtensionActivity;
import com.ihwdz.android.hwapp.ui.orders.payment.PaymentActivity;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract.MODE_EXTENSION;
import static com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract.MODE_PAY;
import static com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract.MODE_INVOICE;
import static com.ihwdz.android.hwapp.ui.orders.detail.OrderDetailContract.MODE_HIDE;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/22
 * desc :  订单详情 （全部订单 待付款 待发货 .. 交易 & 商家）
 * version: 1.0
 * </pre>
 */
public class OrderDetailPresenter implements OrderDetailContract.Presenter{

    String TAG = "OrderDetailPresenter";

    @Inject OrderDetailActivity parentActivity;
    @Inject OrderDetailContract.View mView;
    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    @Inject OrderItemAdapter mAdapter;

    private int currentMode = MODE_HIDE;


    private String currentOrderId;
    private String currentOrderSn;     // pay
    private String currentGoodsAmount; // pay
    
    
    

    @Inject
    public OrderDetailPresenter(OrderDetailActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(activity);

        // order status
        waitCheck = parentActivity.getResources().getString(R.string.order_wait_check);    // 待审核
        waitPay = parentActivity.getResources().getString(R.string.order_wait_pay);      // 待付款
        waitCollection = parentActivity.getResources().getString(R.string.order_wait_collection);   // 待收款
        waitDeliver = parentActivity.getResources().getString(R.string.order_wait_deliver);  // 待发货
        waitTake = parentActivity.getResources().getString(R.string.order_wait_take);     // 待收货
        waitInvoice = parentActivity.getResources().getString(R.string.order_wait_invoice);     // 待开票
        orderSuccess = parentActivity.getResources().getString(R.string.order_success);    // 交易成功
        orderFailure = parentActivity.getResources().getString(R.string.order_failure);    // 交易失败
        waitConfirm = parentActivity.getResources().getString(R.string.order_wait_confirm);    // 待付款/待收货 判断还款是否完成（加逾期）？？？？？？？？？？

        chargeStr = parentActivity.getResources().getString(R.string.charges);        // 手续费：
        totalStr = parentActivity.getResources().getString(R.string.total);           // 合计：

        // button style: orange ring + orange text
        buttonBg = parentActivity.getResources().getDrawable(R.drawable.orange_ring_button);
        buttonColor = parentActivity.getResources().getColor(R.color.orangeTab);

        // button text
        // cancelBtStr = parentActivity.getResources().getString(R.string.order_cancel);         // 取消订单
        payBtStr = parentActivity.getResources().getString(R.string.order_pay);                  // 支付手续费
        extensionBtStr = parentActivity.getResources().getString(R.string.extension_apply_title);// 申请展期
        invoiceBtStr = parentActivity.getResources().getString(R.string.invoice_apply);  // 申请开票
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
    public OrderItemAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setCurrentMode(int mode) {
        if (currentMode != mode){
            currentMode = mode;
        }
        switch (currentMode){
            case MODE_PAY:        // 支付手续费
                mView.showBottomLinear(payBtStr);
                break;
            case MODE_EXTENSION: // 申请展期
                mView.showBottomLinear(extensionBtStr);
                break;
            case MODE_INVOICE:   // 申请开票
                mView.showBottomLinear(invoiceBtStr);
                break;
            case MODE_HIDE:      // 隐藏按钮
                mView.hideBottomLinear();
                break;
            default:
                mView.hideBottomLinear();
                break;

        }
    }

    @Override
    public int getCurrentMode() {
        return currentMode;
    }

    @Override
    public void setVipMode(int type) {
        this.vipMode = type;
    }

    @Override
    public void setCurrentId(String id) {
        this.currentOrderId = id;
    }

    @Override
    public void getOrderDetailData() {

        LogUtils.printCloseableInfo(TAG, "currentQuoteId: " + currentOrderId);
        Subscription rxSubscription = model
                .getOrderDetailData(currentOrderId)
                .compose(RxUtil.<OrderDetailData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderDetailData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printCloseableInfo(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onNext(OrderDetailData data) {
                        LogUtils.printCloseableInfo(TAG, "onNext: id " + data.data.id);
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                setOrderDetailData(data.data);
                                mView.updateView(data.data);
                            }else {
                                LogUtils.printCloseableInfo(TAG, "onNext: data.data == null " + data.msg);
                            }
                        }else {
                            LogUtils.printCloseableInfo(TAG, "onNext: " + data.msg);
                            mView.showPromptMessage(data.msg);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);

    }

    private void setOrderDetailData(OrderDetailData.OrderDetailEntity data) {
        LogUtils.printCloseableInfo(TAG, "setOrderDetailData  id: "+ data.id);
        LogUtils.printCloseableInfo(TAG, "setOrderDetailData  subStatus: "+ data.subStatus);
        this.currentOrderSn = data.orderSn;
        this.currentGoodsAmount = data.serviceCharge;

    }

    @Override
    public void buttonClicked() {
        switch (currentMode){
            case MODE_PAY:
                gotoPay();
                break;
            case MODE_EXTENSION:
                gotoExtension();
                break;
            case MODE_INVOICE:
                gotoInvoice();
                break;
            default:
                break;

        }
    }

    // 申请展期
    @Override
    public void gotoExtension() {
        ExtensionActivity.startExtensionActivity(parentActivity, currentOrderId);
    }


    // 申请开票
    @Override
    public void gotoInvoice() {
        //mView.showPromptMessage("申请开票");
        InvoiceActivity.startInvoiceActivity(parentActivity, currentOrderId);
    }

    // TODO: 2018/12/6  支付手续费 currentOrderSn? currentGoodsAmount?
    @Override
    public void gotoPay() {
        //mView.showPromptMessage("支付手续费");
        LogUtils.printCloseableInfo(TAG, "支付手续费  token: "+ Constant.token);
        LogUtils.printCloseableInfo(TAG, "支付手续费  currentOrderId: "+ currentOrderId);
        LogUtils.printCloseableInfo(TAG, "支付手续费  currentOrderId: "+ currentOrderSn);
        LogUtils.printCloseableInfo(TAG, "支付手续费  currentGoodsAmount: "+ currentGoodsAmount);
        PaymentActivity.startPaymentActivity(parentActivity, currentOrderId, currentOrderSn, currentGoodsAmount);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////
    // 支付手续费 ，申请展期， 申请开票等完成后 返回更新 订单详情

    private int vipMode = 0;     // 0 - 交易会员；1 - 商家会员；
    // 按钮状态
    private Drawable buttonBg;
    private int buttonColor;
    private String buttonStr;       // 按钮文字
    // private String cancelBtStr;  // 取消订单 -》 cancelled now
    private String extensionBtStr;  // 申请展期
    private String invoiceBtStr;    // 申请展期
    private String payBtStr;        // 支付手续费

    // 订单状态
    private String statusStr;       // 订单状态文字
    private String waitCheck;       // 待审核
    private String waitPay;         // 待付款
    private String waitCollection;  // 待收款
    private String waitDeliver;     // 待发货
    private String waitTake;        // 待收货
    private String waitInvoice;     // 待开票
    private String orderSuccess;    // 交易成功
    private String orderFailure;    // 交易失败
    private String waitConfirm;     // 待付款/待收货 判断还款是否完成（加逾期）？？？？？？？？？？

    private String chargeStr, totalStr; // 手续费，合计


    /**
     *   订单状态 (后台状态：买：订单状态 | 卖：订单状态)
     */
    @Override
    public String getStatusTag(String status){
        String result = "";
        if (status != null){
            int statusInt = Integer.valueOf(status);
            switch (statusInt){
                case 10: //待审核： 待审核（买） - 取消订单 | 待审核（卖）
                    statusStr = waitCheck;
                    break;
                case 11: //待上传采购合同： 待发货（买） - 取消订单 | 待收款（卖）
                    if (vipMode == 0){
                        statusStr = waitDeliver;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 12: //待支付手续费： 待付款（买） - 取消订单 | 待收款（卖）
                    if (vipMode == 0){
                        statusStr = waitPay;
                    }else {
                        statusStr = waitCollection;
                    }

                    break;
                case 20: //待收款： 待付款（买） - 取消订单 | 待收款（卖）
                    if (vipMode == 0){
                        statusStr = waitPay;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 30: //待付款：  待发货（买） - 取消订单 | 待收款（卖）
                    if (vipMode == 0){
                        statusStr = waitDeliver;
                    }else {
                        statusStr = waitCollection;
                    }
                    break;
                case 40: //待发货：  待发货（买） - 取消订单 | 待发货（卖）
                    statusStr = waitDeliver;
                    break;
                case 50: //待确认收货：  待发货（买） - 取消订单 | 待收货（卖）
                    if (vipMode == 0){
                        statusStr = waitConfirm;
                    }else {
                        statusStr = waitTake;
                    }
                    break;
                case 90: //交易失败： 交易失败（买） - 取消订单 | 交易失败（卖）
                    statusStr = orderFailure;
                    break;
                case 100: //待开票： 待开票（买） - 取消订单 | 交易成功（卖）
                    if (vipMode == 0){
                        statusStr = waitInvoice;
                    }else {
                        statusStr = orderSuccess;
                    }
                    break;
                case 101: //待开票审核： 待开票（买） - 取消订单 | 交易成功（卖）
                    if (vipMode == 0){
                        statusStr = waitInvoice;
                    }else {
                        statusStr = orderSuccess;
                    }
                    break;
                case 102: //交易成功： 交易成功（买） - 交易成功 | 待收款（卖）
                    statusStr = orderSuccess;
                    break;
            }
            result = statusStr;
        }else {
            LogUtils.printCloseableInfo(TAG, "=======    status == null   =======");
        }
        return result;
    }



}
