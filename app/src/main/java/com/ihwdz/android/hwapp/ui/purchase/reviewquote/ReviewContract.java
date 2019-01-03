package com.ihwdz.android.hwapp.ui.purchase.reviewquote;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/19
 * desc :  /**
 * 备注:
 *   交易会员:
 *   价格复议（求购报价-价格复议）“确认复议”
 *   申请展期备注（订单列表-申请展期）“提交申请”
 *   授信额度 （一键下单-结算方式:部分授信）“确认”
 *
 *   商家会员:
 *   复议报价 （我的报价-复议报价）“确认报价”
 * version: 1.0
 * </pre>
 */
public interface ReviewContract {

    interface View extends BaseView {

        void updatePurchaseView();      // 价格复议 - 买家 - 备注

        void updateExtensionView();     // 申请展期 - 买家 - 备注
        void updateCreditView();        // 授信额度 - 买家 - 数字

        void updateQuoteView();         // 复议价格 - 商家 - 数字

        void showPromptMessage(String message);   // 提示信息
    }


    interface Presenter extends BasePresenter {

        void setCurrentMode(int mode);
        int getCurrentMode();

        void setCurrentId(String id);

        void setCreditMax(double max1, double max2);

        void setReviewContent(String content);    // 备注内容

        void doClickButton();        // 点击按钮

        void doConfirmReview();      // 确认复议
        void doConfirmQuote();       // 确认报价 - 商家复议报价
    }
}
