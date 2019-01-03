package com.ihwdz.android.hwapp.ui.orders.payment;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/29
 * desc : 支付页面 （选择支付宝/微信 支付）
 * version: 1.0
 * </pre>
 */
public interface PaymentContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void showPromptMessage(String message); //  提示信息
    }


    interface Presenter extends BasePresenter {

        void setOrderId(String id);
        void setOrderSn(String orderSn);
        void setGoodsAmount(String amount);  // 获取支付金额

        // void getPaymentAmount();  // 获取支付金额

        boolean getIsAliPaySubmitClicked();   // 提交按钮 是否点击
        boolean getIsWeChatPaySubmitClicked();// 提交按钮 是否点击
        void setIsWeChatPaySubmitClicked(boolean isClicked);


        void postWeChatPayData();  // 微信支付
        void postAliPayData();     // 支付宝支付

        String getOrderInfo();
        String getAppId();
        String getPartnerId ();
        String getPrepayId();
        String getPackageValue ();
        String getNonceStr();
        String getTimeStamp();
        String getSign();

        void goBack();                   // 返回订单详情页 更新按钮状态

    }



}
