package com.ihwdz.android.hwapp.ui.me.collections;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.me.records.RecordAdapter;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/24
 * desc :    我的收藏
 * version: 1.0
 * </pre>
 */
public interface CollectionContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();
        void showPromptMessage(String string); //  提示信息
        void showEmptyView();     // 没有数据时显示

    }


    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        void refreshData();
        CollectionAdapter getAdapter();
        void getCollectionData();        // 我的收藏数据
    }
}
