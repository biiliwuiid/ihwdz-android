package com.ihwdz.android.hwapp.ui.home.infoday;

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
public interface InfoDayContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void updateRecyclerView(boolean isNews);

        void showPromptMessage(String message);
    }

    int ALL_DATA = 0;
    int PRICE_ADJUST_DATA = 1; // 企业调价
    int DYNAMIC_DATA = 2;      // 装置动态

    int FY_DATA = 3;  // 市场风云
    int SD_DATA = 4;  // 深度研报
    int HY_DATA = 5;  // 行业热点
    int TT_DATA = 6;  // 宏观头条

    int DEFAULT_COUNT = 6;


    interface Presenter extends BasePresenter {

        InfoTitleAdapter getTitleAdapter();
        InfoDayAdapter getAdapter();
        NewsAdapter getNewsAdapter();

        void setIsNewsContent(boolean isNews);
        boolean getIsNewsContent();

        void setCurrentMode(int mode);
        int getCurrentMode();

        void setYesterday(String yesterday);
        String getYesterday();

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        void refreshData();

        void getAllData();
        void getPriceAdjustInfoData();  // 企业调价
        void getDynamicInfoData();      // 装置动态

        void getFyNewsData(int pageNum);           // 市场风云
        void getSdNewsData(int pageNum);           // 深度研报
        void getHyNewsData(int pageNum);           // 行业热点
        void getTtNewsData(int pageNum);           // 宏观头条


    }
}
