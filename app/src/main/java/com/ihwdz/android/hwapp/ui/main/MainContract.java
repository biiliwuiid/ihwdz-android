package com.ihwdz.android.hwapp.ui.main;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.ihwdz.android.hwapp.base.listener.OnLoginSuccessListener;
import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

import java.util.ArrayList;

/**
 * <pre>
 * author : Duan
 * time : 2018/7/24.
 * desc : MainContract
 * version: 1.0
 * </pre>
 */

public interface MainContract {

    interface View extends BaseView {
        void showAdDialog();    // 广告弹窗 -  initView

        int getCurrentFragment();
        void showPromptMessage(String string);
    }

    interface Presenter extends BasePresenter {
        ArrayList<CustomTabEntity> getTabEntity();
        void getUpdate();
        // 判断是否为首次加载
        boolean getIsFirstLoad();
        void setIsFirstLoad(boolean isFirstLoad);

        void goAdvertisement();

        boolean getIsLogout();
        void setIsLogout(boolean isLogout);

        // 尝试自动登录
        void login();
        void getUserType();        // 用户类型
        void getUserStatus();      // 用户信息： 是否认证；申请状态

    }
}
