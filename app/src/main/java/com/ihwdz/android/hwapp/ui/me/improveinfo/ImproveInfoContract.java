package com.ihwdz.android.hwapp.ui.me.improveinfo;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/30
 * desc :   完善用户信息
 * version: 1.0
 * </pre>
 */
public interface ImproveInfoContract {

    interface View extends BaseView {

        void showPromptMessage(String message); //  提示信息

    }

    interface Presenter extends BasePresenter {

        void gotoUpdateName();          // 修改 姓名
        void gotoUpdateEmail();         // 修改 邮箱
        void updateAddress();           // 修改 省市区
        void gotoUpdateDetailAddress(); // 修改 详细地址

    }

}
