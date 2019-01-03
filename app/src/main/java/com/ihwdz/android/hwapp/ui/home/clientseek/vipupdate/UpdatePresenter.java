package com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;
import com.ihwdz.android.hwapp.ui.home.clientseek.ClientActivity;
import com.ihwdz.android.hwapp.ui.orders.payment.PaymentReceiver;
import com.ihwdz.android.hwapp.utils.log.LogUtils;
import com.ihwdz.android.hwapp.utils.pay.AuthResult;
import com.ihwdz.android.hwapp.utils.pay.PayResult;
import com.ihwdz.android.hwapp.utils.rxUtils.RxUtil;
import com.ihwdz.android.hwslimcore.API.clientData.ClientDataModel;
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
 * time : 2018/10/18
 * desc :  会员升级权益
 * version: 1.0
 * </pre>
 */
public class UpdatePresenter implements UpdateContract.Presenter {

    String TAG = "UpdatePresenter";
    @Inject UpdateActivity parentActivity;
    @Inject UpdateContract.View mView;

    @Inject CompositeSubscription mSubscriptions;
    ClientDataModel model;

    private boolean isAliPaySubmitClicked = false;     // 支付宝支付 是否点击
    private boolean isWeChatPaySubmitClicked = false;  //   微信支付 是否点击

    private String currentCardId;
    private String currentDays;
    private String orderInfo;

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


    private void regToWx(){
        api = WXAPIFactory.createWXAPI(parentActivity.getBaseContext(), APP_ID);
        api.registerApp(APP_ID);

    }

    @Inject
    public UpdatePresenter(UpdateActivity activity){
        this.parentActivity = activity;
        //model = new ClientDataModel(parentActivity);
        model = ClientDataModel.getInstance(parentActivity);
        // 注册微信支付
        // regToWx();
        api = WXPayEntryActivity.getApiInstance(parentActivity, Constant.PayMode.PAY_UPDATE);

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
    public void getUserGoodsData() {

        Subscription rxSubscription = model
                .getUserGoodsData()
                .compose(RxUtil.<UserGoodsData>rxSchedulerHelper())
                .subscribe(new Subscriber<UserGoodsData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(UserGoodsData data) {
                        if (data.data != null){
                            mView.updateView(data);
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
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
        this.isWeChatPaySubmitClicked = isClicked;
    }

    @Override
    public void postWeChatPayData() {
        isAliPaySubmitClicked = false;
        isWeChatPaySubmitClicked = true;
        LogUtils.printCloseableInfo(TAG, "=========  postWeChatPayData ======= currentCardId: "+ currentCardId +"     currentDays: "+ currentDays);
        Subscription rxSubscription = model
                .postWeChatPayData(currentCardId, 12)
                .compose(RxUtil.<OrderPayData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderPayData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        isWeChatPaySubmitClicked = false;
                        e.printStackTrace();
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

                                LogUtils.printCloseableInfo(TAG, "postWeChatPayData  onNext ===============================================");
                                LogUtils.printCloseableInfo(TAG, "=======  appId: " + appId);
                                LogUtils.printCloseableInfo(TAG, "=======  partnerId: " + partnerId);
                                LogUtils.printCloseableInfo(TAG, "=======  prepayId: " + prepayId);
                                LogUtils.printCloseableInfo(TAG, "=======  packageValue: " + packageValue);
                                LogUtils.printCloseableInfo(TAG, "=======  nonceStr: " + nonceStr);
                                LogUtils.printCloseableInfo(TAG, "=======  timeStamp: " + timeStamp);
                                LogUtils.printCloseableInfo(TAG, "=======  sign: " + sign);
                                LogUtils.printCloseableInfo(TAG, "===============================================");

                                weChatPay();
                            }else {
                                mView.showPromptMessage(data.msg);
                            }
                        }else {
                            isWeChatPaySubmitClicked = false;
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void postAliPayData() {
        isAliPaySubmitClicked = true;
        isWeChatPaySubmitClicked = false;
        LogUtils.printCloseableInfo(TAG, "=========  postAliPayData ======= currentCardId: "+ currentCardId +"     currentDays: "+ currentDays);
        Subscription rxSubscription = model
                .postAliPayData(currentCardId, 12)
                .compose(RxUtil.<OrderData>rxSchedulerHelper())
                .subscribe(new Subscriber<OrderData>() {
                    @Override
                    public void onCompleted() {
                        mView.hideWaitingRing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        isAliPaySubmitClicked = false;
                        Log.d(TAG, "onError: " + e.toString());
                        e.printStackTrace();
                        mView.showPromptMessage(e.toString());
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
                            mView.showPromptMessage(data.msg);
                        }

                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void setCardId(String id) {
        this.currentCardId = id;
    }

    @Override
    public void setDays(String days) {
        this.currentDays = days;
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
    public void backToClientActivity() {
        parentActivity.onBackClicked();
//        Intent intent = new Intent(parentActivity.getBaseContext(), ClientActivity.class);
//        parentActivity.startActivity(intent);
//        parentActivity.finish();
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
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();
                    LogUtils.printCloseableInfo(TAG, "Ali resultStatus =========== : " + resultStatus);
                    LogUtils.printCloseableInfo(TAG, "Ali memo =========== : " + memo);

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Constant.updateOptionDone = true;  // 支付宝支付成功
                        mView.showPromptMessage("支付成功");
                        // Toast.makeText(parentActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        backToClientActivity();
                    }else {
                        isAliPaySubmitClicked = false;
                         mView.showPromptMessage(memo);
//                        if (TextUtils.equals(resultStatus, "6001")){   //操作已经取消
//                            mView.showPromptMessage("已取消支付");
//                            isAliPaySubmitClicked = false;
//                            // Toast.makeText(parentActivity, "已取消支付", Toast.LENGTH_SHORT).show();
//                        } else {
//                            mView.showPromptMessage("支付失败");
//                            isAliPaySubmitClicked = false;
//                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                            // Toast.makeText(parentActivity, "支付失败", Toast.LENGTH_SHORT).show();
//                            // backToClientActivity();
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

//        LogUtils.printCloseableInfo(TAG, "weChatPay ===============================================");
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
