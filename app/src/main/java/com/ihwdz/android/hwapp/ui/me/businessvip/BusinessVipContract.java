package com.ihwdz.android.hwapp.ui.me.businessvip;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.ui.main.MainActivity;

/**
 * <pre>
 * author : Duan
 * time : 2018/09/15
 * desc : 商家会员
 * version: 1.0
 * </pre>
 */
public interface BusinessVipContract {

    interface View extends BaseView {


    }

    interface Presenter extends BasePresenter {

        void bindActivity(MainActivity activity);

        void gotoCollections();    // 显示　我的收藏
        void gotoMsgCenter();      // 显示　消息中心

        void gotoUpdateUserInfo(); // 显示　完善信息

        void gotoMyVipInfo();      // 显示  我的商家会员

        void gotoLogistics();      // 显示  物流小助手
        void gotoSettings();       // 显示　设置

    }
}
