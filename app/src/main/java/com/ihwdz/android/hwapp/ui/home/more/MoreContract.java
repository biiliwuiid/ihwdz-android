package com.ihwdz.android.hwapp.ui.home.more;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/30
 * desc :
 * version: 1.0
 * </pre>
 */
public interface MoreContract {
    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

    }

    int HW24H_MODE = 0;
    int REC_ABC_MODE = 1;
    int REC_MODE = 2;

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        Hw24hAdapter getHw24Adapter();  // Hw24h more adapter
        NewsAdapter getNewsAdapter();   // Recommend more adapter

        void setCurrentMode(int mode);
        int getCurrentMode();

        void refreshData();

        void getHw24hData();        // 鸿网２４小时　更多
        void getRecommendABCData(); // 推荐更多  行业ABC
        void getRecommendData();    // 推荐更多  行业研究

    }
}
