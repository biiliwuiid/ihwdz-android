package com.ihwdz.android.hwapp.ui.purchase.purchasedetail;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.PurchaseQuoteData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :  求购报价： 我的求购 -item-> 求购报价（交易会员）
 * version: 1.0
 * </pre>
 */
public interface PurchaseDetailContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void updatePurchaseView(PurchaseQuoteData.PurchaseEntity data); // 更新求购信息
        void updateQuoteView(int quoteCount);     // 更新报价商家数目

        void showRemindDialog(String message); // 用户状态异常（锁定、认证） 联系客服

        void showPromptMessage(String message);   // 提示信息

        void showEmptyView();
        void hideEmptyView();
    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        PurchaseQuoteAdapter getAdapter();
        void refreshData();

        void getMyPurchaseListData(); // 获取 我的求购 数据

        void getUserType();           // 判断用户类型、锁定、认证状态
        void getOrderCheckData();     // 检验能否一键下单

        void setCurrentId(String id); // 我的 当前该条求购的 id

        void setCurrentQuoteId(String id); // 我的 当前该条求购的 某条报价的id

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        void doOrder();               // 点击：一键下单 --检验能否一键下单--> 确认订单
        void doReview(String id);     // 点击：价格复议

        void gotoOrderConfirm();      // -> 确认订单

        android.view.View getDialogContentView(String message);         // 弹窗
        void doContract();                                              // 联系客服

    }
}
