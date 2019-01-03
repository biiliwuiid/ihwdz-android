package com.ihwdz.android.hwapp.ui.home.priceinquiry.collections;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/12/21
 * desc :   查价格 - 我的收藏
 * version: 1.0
 * </pre>
 */
public interface PriceCollectionContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();

        void showEmptyView();
        void hideEmptyView();

        void showDialog(int collectionType, String breed, String spec, String brand, String area);      // 取消收藏 确认

        void updateDateTitle(String title);    // 更新日期

        void showPromptMessage(String message);

    }

    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        PriceCollectionAdapter getAdapter();       // adapter for price data recycler view

        void refreshData();
        void getPriceCollectedData();              // 收藏数据

        void doCollect(int collectionType,       // 0 市场价 1 出厂价
                       String breed,
                       String spec,
                       String brand,
                       String area);                          // 收藏动作

        void doCollectCancel(int collectionType,       // 0 市场价 1 出厂价
                             String breed,
                             String spec,
                             String brand,
                             String area);                    // 取消收藏动作

        int getCurrentPageCount();
        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        android.view.View  getCancelCollectView(String message);         // 取消收藏 弹框内容

        void selectDate();                   // 日期选择器
        void setCurrentDate(String date);    // 选中日期


    }

}
