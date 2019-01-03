package com.ihwdz.android.hwapp.ui.me.vipinformation;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;
import com.ihwdz.android.hwapp.model.bean.UserInformation;

/**
 * <pre>
 * author : Duan
 * time : 2018/10/12
 * desc :  会员信息  只展示信息 不修改
 * version: 1.0
 * </pre>
 */
public interface VipInfoContract {

    interface View extends BaseView {
        void showVipInfo(UserInformation.UserInfo info);
        void showPromptMessage(String string); //  提示信息
    }

    interface Presenter extends BasePresenter {

        void getVipInfo();            //　获取会员信息
        void goSelectDistrict();      //  选择地区
        void postUserUpdate();
    }
}
