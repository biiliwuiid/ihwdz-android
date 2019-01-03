package com.ihwdz.android.hwapp.ui.publish;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/04
 * desc :   发布 Fragment
 * version: 1.0
 * </pre>
 */
public interface PublishContract {

    interface View extends BaseView {

        void showWaitingView(); // 敬请期待

        void showBoth();        // 发布求购 & 我要报价
        void showPurchaseView();// 发布求购
        void showQuoteView();   // 我要报价

        void showPromptMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        void getUserType();         // 判断用户类型、锁定、认证状态

        void gotoLoginPage();        // 未登录用户跳转登录页面（both）
        void gotoApplyDealVip();     // 申请开通交易会员  （我要采购）
        void gotoPublishPurchase();  // 发布求购 - 交易会员 （我要采购）
        void gotoPurchasePool();     // 求购池 - 商家会员 （我要报价）

    }
}
