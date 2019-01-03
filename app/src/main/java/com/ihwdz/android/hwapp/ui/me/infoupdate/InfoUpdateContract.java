package com.ihwdz.android.hwapp.ui.me.infoupdate;

import com.ihwdz.android.hwapp.base.mvp.BasePresenter;
import com.ihwdz.android.hwapp.base.mvp.BaseView;

/**
 * <pre>
 * author : Duan
 * time : 2018/11/30
 * desc :   修改-保存数据 :
 *           用户信息；
 *           报价-单价；
 * version: 1.0
 * </pre>
 */
public interface InfoUpdateContract {

    interface View extends BaseView {

        void showPromptMessage(String message); //  提示信息

    }


    interface Presenter extends BasePresenter {

        void setCurrentMode(int mode);

        void setCurrentContent(String content);

        void doSave();                  // 点击保存

        void postUserInformation();     // post用户信息

    }

}
