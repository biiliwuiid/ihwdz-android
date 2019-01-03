package com.ihwdz.android.hwapp.ui.me.settings;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/18
 * desc :
 * version: 1.0
 * </pre>
 */
public interface SettingContract {

    interface View extends BaseView {

        void showPersonalCenter();                    // 显示个人中心未登录状态　

        void showPromptMessage(String message);       //  提示信息

    }

    interface Presenter extends BasePresenter {
        void logout();      //　退出登录
    }
}
