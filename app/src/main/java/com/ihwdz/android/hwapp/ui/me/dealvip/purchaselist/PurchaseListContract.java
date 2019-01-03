package com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/20
 * desc :  我的求购单 not use now
 * version: 1.0
 * </pre>
 */
public interface PurchaseListContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showPromptMessage(String msg);
        void showEmptyView();

    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        void refreshData();
        PurchaseListAdapter getAdapter();
        void getPurchaseData();            // 求购单
    }
}
