package com.ihwdz.android.hwapp.ui.home.clientseek.vipupdate;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.UserGoodsData;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/18
 * desc :
 * version: 1.0
 * </pre>
 */
public interface UpdateContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void updateView(UserGoodsData data);
        void showPromptMessage(String string); //  提示信息

    }


    interface Presenter extends BasePresenter {

        void refreshData();

        void getUserGoodsData();   // 麻雀 凤凰 喜鹊...

        boolean getIsAliPaySubmitClicked();// 提交按钮 是否点击
        boolean getIsWeChatPaySubmitClicked();// 提交按钮 是否点击
        void setIsWeChatPaySubmitClicked(boolean isClicked);

        void postWeChatPayData();  // 微信支付
        void postAliPayData();     // 支付宝支付

        void setCardId(String id);
        void setDays(String days);



        String getOrderInfo();
        String getAppId();
        String getPartnerId ();
        String getPrepayId();
        String getPackageValue ();
        String getNonceStr();
        String getTimeStamp();
        String getSign();

        void backToClientActivity();


    }

}
