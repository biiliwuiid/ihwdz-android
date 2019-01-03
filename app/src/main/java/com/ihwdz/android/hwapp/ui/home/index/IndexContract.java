package com.ihwdz.android.hwapp.ui.home.index;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :
 * version: 1.0
 * </pre>
 */
public interface IndexContract {

    interface View extends BaseView {

        void showWaitingRing();  // "查看更多"、"上拉加载更多"
        void hideWaitingRing();

    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        IndexViewAdapter getAdapter();
        void refreshData();
        void getAllData();
        void getAllData(int pageNum, int pageSize);

    }
}
