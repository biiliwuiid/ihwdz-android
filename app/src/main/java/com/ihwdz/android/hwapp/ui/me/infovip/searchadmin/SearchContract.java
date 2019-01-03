package com.ihwdz.android.hwapp.ui.me.infovip.searchadmin;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.me.dealvip.purchaselist.PurchaseListAdapter;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/10
 * desc :
 * version: 1.0
 * </pre>
 */
public interface SearchContract {


    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();
        void showMsg(String msg);
        void showEmptyView();

    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        void refreshData();
        FuzzySearchAdapter getFuzzySearchAdapter();
        AZListAdapter getAdapter();
//        AdminAdapter getAdapter();


        void getAdminData();                            // 获取 管理员信息

    }
}
