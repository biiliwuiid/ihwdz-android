package com.ihwdz.android.hwapp.ui.orders.payment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ihwdz.android.hwapp.common.Constant;
import com.ihwdz.android.hwapp.model.bean.OrderData;
import com.ihwdz.android.hwapp.model.bean.OrderPayData;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.pay.AuthResult;
import com.ihwdz.android.hwapp.utils.pay.PayResult;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.logisticsData.LogisticsModel;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import javax.inject.Inject;

import io.dcloud.ihwdz.wxapi.WXPayEntryActivity;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/29
 * desc :   支付页面 （选择支付宝/微信 支付）
 * version: 1.0
 * </pre>
 */
public class PaymentPresenter implements PaymentContract.Presenter {


    String TAG = "PaymentPresenter";
    @Inject PaymentActivity parentActivity;
    @Inject PaymentContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    private LogisticsModel model;

    private String orderInfo;

    private String currentOrderId;
    private String currentOrderSn;
    private String currentGoodsAmount;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018050960133152";    // 支付宝
    public static final String APP_ID = "wx51ee42f8a498e2aa"; // 微信
    private IWXAPI api;

    // weChat param
    private String appId ;
    private String partnerId ;
    private String prepayId;
    private String packageValue = "Sign=WXPay";
    private String nonceStr;
    private String timeStamp;
    private String sign;
    /**
     应用签名：3fa2f63e8dd9a8eea54b3cc8161cf65d
     包名：io.dcloud.ihwdz
     */

    private boolean isAliPaySubmitClicked = false;     // 支付宝支付 是否点击
    private boolean isWeChatPaySubmitClicked = false;  //   微信支付 是否点击

    private void regToWx(){
        api = WXAPIFactory.createWXAPI(parentActivity.getBaseContext(), APP_ID);
        api.registerApp(APP_ID);
    }

    @Inject
    public PaymentPresenter(PaymentActivity activity){
        this.parentActivity = activity;
        model = LogisticsModel.getInstance(parentActivity);

        // regToWx();
        api = WXPayEntryActivity.getApiInstance(parentActivity, Constant.PayMode.PAY_CHARGE);
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
    public void setOrderId(String id) {
        this.currentOrderId = id;
    }

    @Override
    public void setOrderSn(String orderSn) {
        this.currentOrderSn = orderSn;
    }

    @Override
    public void setGoodsAmount(String amount) {
        this.currentGoodsAmount = amount;
    }

    @Override
    public boolean getIsAliPaySubmitClicked() {
        return isAliPaySubmitClicked;
    }

    @Override
    public boolean getIsWeChatPaySubmitClicked() {
        return isWeChatPaySubmitClicked;
    }

    @Override
    public void setIsWeChatPaySubmitClicked(boolean isClicked) {
        isWeChatPaySubmitClicked = isClicked;
    }


    @Override
    public void postWeChatPayData() {
        isWeChatPaySubmitClicked = true;
        isAliPaySubmitClicked = false;
        LogUtils.printCloseableInfo(TAG, "========= postWeChatPayData ");
//        LogUtils.printCloseableInfo(TAG, "=========  currentOrderId ======= : " + currentOrderId );
//        LogUtils.printCloseableInfo(TAG, "=========  currentOrderSn ======= : " + currentOrderSn );
//        LogUtils.printCloseableInfo(TAG, "=========  currentGoodsAmount ======= : " + currentGoodsAmount );
        Subscription rxSubscription = model
                .postWeChatPayData(currentOrderId, currentOrderSn, currentGoodsAmount)
                .compose(RxUtil.<OrderPayData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderPayData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        isWeChatPaySubmitClicked = false;
                        mView.showPromptMessage(e.toString());
                    }

                    @Override
                    public void onNext(OrderPayData data) {
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                appId = data.data.appId;
                                partnerId = data.data.mchId;
                                prepayId = data.data.prepayId;
                                nonceStr = data.data.nonceStr;
                                timeStamp = data.data.timestamp;
                                sign = data.data.sign;
                                weChatPay();
                            }else {
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            isWeChatPaySubmitClicked = false;
                            mView.showPromptMessage(data.msg);
                            LogUtils.printCloseableInfo(TAG, "========= postWeChatPayData  onNext ======= : " + data.msg );
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void postAliPayData() {
        isAliPaySubmitClicked = true;
        isWeChatPaySubmitClicked = false;
        LogUtils.printCloseableInfo(TAG, "========= postAliPayData ");
//        LogUtils.printCloseableInfo(TAG, "=========  currentOrderId ======= : " + currentOrderId );
//        LogUtils.printCloseableInfo(TAG, "=========  currentOrderSn ======= : " + currentOrderSn );
//        LogUtils.printCloseableInfo(TAG, "=========  currentGoodsAmount ======= : " + currentGoodsAmount );
//        LogUtils.printCloseableInfo(TAG, "=========  token ======= : " + Constant.token);

        Subscription rxSubscription = model
                .postAliPayData(currentOrderId, currentOrderSn, currentGoodsAmount)
                .compose(RxUtil.<OrderData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.printError(TAG, "postAliPayData : onError: " + e.toString());
                        e.printStackTrace();
                        isAliPaySubmitClicked = false;
                    }

                    @Override
                    public void onNext(OrderData data) {
                        if (TextUtils.equals("0", data.code)){
                            if (data.data != null){
                                orderInfo = data.data;
                                aliPay();
                            }
                        }else {
                            isAliPaySubmitClicked = false;
                            LogUtils.printCloseableInfo(TAG, "========= postAliPayData  onNext ======= : " + data.msg );
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public String getOrderInfo() {
        return orderInfo;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public String getPartnerId() {
        return partnerId;
    }

    @Override
    public String getPrepayId() {
        return prepayId;
    }

    @Override
    public String getPackageValue() {
        return packageValue;
    }

    @Override
    public String getNonceStr() {
        return nonceStr;
    }

    @Override
    public String getTimeStamp() {
        return timeStamp;
    }

    @Override
    public String getSign() {
        return sign;
    }

    @Override
    public void goBack() {
        parentActivity.onBackClicked();
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult(); // 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();
                    LogUtils.printCloseableInfo(TAG, "Ali resultStatus =========== : " + resultStatus);
                    LogUtils.printCloseableInfo(TAG, "Ali memo =========== : " + memo);
                    //LogUtils.printCloseableInfo(TAG, "Ali resultStatus =========== : " + resultStatus);

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {    //支付成功
                        // TODO: 2018/11/29  支付成功
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        mView.showPromptMessage("支付成功");
                        // Toast.makeText(parentActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        Constant.orderOptionDone = true;  // 支付宝支付成功
                        goBack();
                    }else {
                        isAliPaySubmitClicked = false;
                        mView.showPromptMessage(memo);
//                        if (TextUtils.equals(resultStatus, "6001")){   //操作已经取消
//                            mView.showPromptMessage("已取消支付");
//                            isAliPaySubmitClicked = false;
//                            // Toast.makeText(parentActivity, "已取消支付", Toast.LENGTH_SHORT).show();
//                        }else {
//                            // TODO: 2018/11/29  支付失败
//                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                            mView.showPromptMessage("支付失败");
//                            // Toast.makeText(parentActivity, "支付失败", Toast.LENGTH_SHORT).show();
//                            isAliPaySubmitClicked = false;
//
//                        }
                    }


                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(parentActivity,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(parentActivity,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    /**
     * 支付宝支付业务
     */
    public void aliPay() {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask aliPayTask = new PayTask(parentActivity);
                Map<String, String> result = aliPayTask.payV2(getOrderInfo(), true);
                LogUtils.printCloseableInfo(TAG + "Ali - msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付业务
     */
    public void weChatPay() {
//        LogUtils.printCloseableInfo(TAG, "===============================================");
//        LogUtils.printCloseableInfo(TAG, "=======  appId: " + getAppId());
//        LogUtils.printCloseableInfo(TAG, "=======  partnerId: " + getPartnerId());
//        LogUtils.printCloseableInfo(TAG, "=======  prepayId: " + getPrepayId());
//        LogUtils.printCloseableInfo(TAG, "=======  packageValue: " + getPackageValue());
//        LogUtils.printCloseableInfo(TAG, "=======  nonceStr: " + getNonceStr());
//        LogUtils.printCloseableInfo(TAG, "=======  timeStamp: " + getTimeStamp());
//        LogUtils.printCloseableInfo(TAG, "=======  sign: " + getSign());
//        LogUtils.printCloseableInfo(TAG, "===============================================");

        PayReq request = new PayReq();
        request.appId = getAppId();
        request.partnerId = getPartnerId();
        request.prepayId = getPrepayId();
        request.packageValue = getPackageValue();
        request.nonceStr = getNonceStr();
        request.timeStamp = getTimeStamp();
        request.sign = getSign();
        api.sendReq(request);

    }

}
