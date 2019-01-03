package com.ihwdz.android.hwapp.ui.purchase.quotedetail;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.QuoteDetailData;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/14
 * desc :  MY_QUOTE -> 报价详情 / QUOTE_BUTTON -> 报价(供配 不需要选择仓库)
 * version: 1.0
 * </pre>
 */
public interface QuoteDetailContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void initQuoteDetailView();     // 报价详情
        void initQuoteConfirmView();    // 确认报价

        void updateView(QuoteDetailData data);   //  更新报价详情
        void updateDate(String date);
        void showPromptMessage(String message);  //  提示信息
    }
    int MODE_QUOTE_DETAIL = 0;   // 报价详情
    int MODE_QUOTE_CONFIRM = 1;  // 确认报价

    interface Presenter extends BasePresenter {

        void getMyQuoteData();    // 获取求购池数据 - 我的报价 详情 - 商家会员已认证

        void setCurrentMode(int mode);
        int getCurrentMode();

        // 确认报价提交数据
        void setCurrentId(String id);                 // 会员求购单Id
        void setCurrentPrice(Double price);           // 报价
        void setIsSupplierDistribution(boolean is);   // 是否供配 0-否 1-是
        void setDeliveryDate(String date);            // 交货日期
        void setWarehouseJson(String warehouseJson);  // 仓库数据

        void updateWarehouse();    // 修改仓库
        void updateDeliveryDate(); // 修改交货日期
        void updatePrice();        // 修改单价


        void storeWarehouse();    // 选中的仓库记录在本地

        boolean checkCurrentDataComplete();

        void gotoMyQuote();       // 提交成功后 -> 我的报价
        void doQuote();           // 确认报价 -> 回到 我的报价
        void doReview();          // 复议报价 -> 我的报价列表里操作

    }

}
