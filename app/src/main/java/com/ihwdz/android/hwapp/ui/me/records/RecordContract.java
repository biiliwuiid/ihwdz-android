package com.ihwdz.android.hwapp.ui.me.records;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.home.more.Hw24hAdapter;
import com.ihwdz.android.hwapp.ui.home.more.NewsAdapter;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/19
 * desc :   交易记录
 * version: 1.0
 * </pre>
 */
public interface RecordContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showPromptMessage(String string); //  提示信息
        void showEmptyView();

    }

    int DEAL_MODE = 0;
    int SHOP_MODE = 1;

    int PageNum = 1;
    int PageSize = 15;

    int InvoiceRequestCode = 0;

    interface Presenter extends BasePresenter {

        void setCurrentMode(int mode);
        int getCurrentMode();

        void setAmount(String amount);
        void setOrderId(String id);
        String getOrderId();

        void gotoInvoiceInfo();

        void refreshData();

        RecordAdapter getAdapter();
        void getRecordData();            // 交易会员交易记录
        void getShopRecordData();        // 商家会员交易记录
    }
}
