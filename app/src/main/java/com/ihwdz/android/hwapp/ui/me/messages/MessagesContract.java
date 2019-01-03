package com.ihwdz.android.hwapp.ui.me.messages;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/09
 * desc :
 * version: 1.0
 * </pre>
 */
public interface MessagesContract {

    interface View extends BaseView {

        void showWaitingRing();
        void hideWaitingRing();
        void showPromptMessage(String message); // 提示信息
        void showEmptyView();                   // 没有数据时显示
        void hideEmptyView();

    }


    int PageNum = 1;
    int PageSize = 15;

    interface Presenter extends BasePresenter {

        void refreshData();
        MessageAdapter getAdapter();
        void getMessageData();        // 我的消息数据
    }

}
