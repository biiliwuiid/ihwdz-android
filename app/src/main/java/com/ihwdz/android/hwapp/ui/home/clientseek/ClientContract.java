package com.ihwdz.android.hwapp.ui.home.clientseek;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/16
 * desc :  找客户
 * version: 1.0
 * </pre>
 */
public interface ClientContract {

    interface View extends BaseView {
        void showWaitingRing();
        void hideWaitingRing();

        void showUpdateButton(boolean hasRight); //  是否显示升级按钮
        void updateView();                       //  更新客户类型选项卡 及客户数据
        void showPromptMessage(String message);  //  提示信息
        void showEmptyView();
        void hideEmptyView();

        void hideKeyboard();
    }

    int PageNum = 1;
    int PageSize = 15;

    int POTENTIAL_CLIENTS_MODE = 0;
    int FOLLOWED_CLIENTS_MODE = 1;
    int KEYWORDS_MODE = 2;

    interface Presenter extends BasePresenter {

        ClientAdapter getAdapter();
        void refreshData();

        void getUpdateRightData();   // 是否已经升级权限
        //void getAllData();

        int getCurrentPageNum();
        void setCurrentPageNum(int pageNum);

        boolean getHasRight();

        void setIsFromFilter(boolean isFromFilter);

        void getPotentialClientsData(int pageNum, int pageSize);    // 潜在客户数据
        void getFollowClientsData(int pageNum, int pageSize);       // 关注客户数据
        void getSearchClientsData(int pageNum, int pageSize);       // 搜索客户数据
        void getFilterClientData(int pageNum, int pageSize);        // 筛选后的客户数据

        void setSearchKeywords(String keywords);

        void setCurrentMode(int mode);
        int getCurrentMode();

        void goLogin();           // 登录/注册提示页
        void goUpdate();          // 升级权限
        void goDetail(String id); // 客户详情
        void goFilter();          // 筛选

    }
}
