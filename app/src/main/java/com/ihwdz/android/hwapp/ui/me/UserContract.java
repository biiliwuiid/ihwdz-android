package com.ihwdz.android.hwapp.ui.me;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

/**
 * <pre>
 * author : Duan
 * time : 2018/07/25
 * desc :　　我的 Fragment - Personal Center (未登录)
 * version: 1.0
 * </pre>
 */
public interface UserContract {

    interface View extends BaseView {


    }

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        void gotoLoginPage();     // 登陆/注册
        void gotoCollections();   // 显示　我的收藏
        void gotoMsgCenter();     // 显示　消息中心

        void gotoLogistics();     // 显示  物流小助手
        void gotoHwVipCn();       // 显示　鸿网公众号
        void gotoAboutUs();       // 显示　关于我们
        void gotoSettings();      // 显示　设置
    }
}
